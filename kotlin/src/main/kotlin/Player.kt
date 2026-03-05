package com.sheepduke.donkeyrace

import kotlin.random.Random

typealias PlayerName = String

data class Player(
        val name: String,
        val feedBag: List<Feed>,
        val consumedFeeds: List<Feed> = listOf(),
        val position: Position = 0,
        val gemCount: Int = 0,
        val sleepCount: Int = 0,
        val cloverCount: Int = 0,
        val strategy: PlayerStrategy,
        val callback: PlayerCallback
) {
    fun addFeed(feed: Feed): Player = copy(feedBag = feedBag + feed)

    fun pickRandomFeed(random: Random = Random): Feed {
        val feed = feedBag.random(random)
        callback.onPickFeed(this, feed)

        return feed
    }

    fun consumeFeed(feed: Feed): Player =
            copy(feedBag = feedBag - feed, consumedFeeds = consumedFeeds + feed)

    fun pickAndConsumeRandomFeed(state: GameState, context: GameContext): Player {
        if (state.ruleset.map.isEndReached(position)) {
            return this
        }

        val feed = pickRandomFeed(context.random)
        val action = state.ruleset.actions[feed.type]

        var player = moveForward(feed.value)
        player = player.consumeFeed(feed)

        callback.onPerformFeedAction(player, action)
        player = action?.performAction(player, feed, state, context) ?: player

        return player
    }

    fun rollDice(state: GameState, context: GameContext): Player {
        val action = state.ruleset.dice.roll(context.random)
        callback.onPerformDiceAction(this, action)

        return action.performAction(this, state, context)
    }

    fun removeConsumedFeed(feed: Feed): Player = copy(consumedFeeds = consumedFeeds - feed)

    fun moveForward(step: Int): Player {
        val newPosition = position + step
        callback.onChangePosition(this, newPosition)

        return copy(position = newPosition)
    }

    fun moveToPosition(position: Position): Player {
        callback.onChangePosition(this, position)

        return copy(position = position)
    }

    fun incrementGemCount(count: Int): Player = copy(gemCount = gemCount + count)

    fun incrementSleepCount(): Player = copy(sleepCount = sleepCount + 1)

    fun incrementCloverCount(count: Int): Player = copy(cloverCount = cloverCount + count)

    fun chooseAndAddFreeFeed(feeds: List<Feed>, state: GameState, context: GameContext): Player =
            copy(feedBag = feedBag + strategy.chooseFreeFeed(this, feeds, state, context))

    fun putBackFeed(state: GameState, context: GameContext): Player {
        val feed = strategy.chooseFeedToPutBack(this, state, context)

        return copy(feedBag = feedBag + feed, consumedFeeds = consumedFeeds - feed)
    }

    fun buyFeeds(state: GameState, context: GameContext): Player {
        // 1. Move forward by clover count.
        var player = moveForward(cloverCount)

        // 2. Calculate the gem count (considering position).
        if (player.position <= state.players.minOf { it.position }) {
            player = player.incrementGemCount(1)
        }

        // 3. Buy feeds and reset state.
        val feedsBought = strategy.buyFeeds(player, state, context)

        callback.onBuyFeeds(player, feedsBought)

        return copy(
                feedBag = feedBag + consumedFeeds + feedsBought,
                consumedFeeds = listOf(),
                gemCount = 0,
                sleepCount = 0
        )
    }
}
