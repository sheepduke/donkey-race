package com.sheepduke.donkeyrace

sealed interface FeedAction {
    fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player

    companion object {
        val BASIC_ACTIONS: Map<FeedType, FeedAction> = mapOf(
            FeedType.GRASS to SleepAction(),
            FeedType.APPLE to AddGemByVortexAction(),
            FeedType.CORN to RollDiceAction(),
            FeedType.CABBAGE to PutBackFeedAction(),
            FeedType.BERRY to MoveToNextVortexAction(),
            FeedType.PRUNE to MoveByGemAction(),
            FeedType.CARROT to AddCloverByVortexAction(),
        )

        val ADVANCED_ACTIONS: Map<FeedType, FeedAction> = mapOf(
            FeedType.GRASS to SleepAction(),
            FeedType.APPLE to AddGemByPriceAction(),
            FeedType.CORN to RollDiceByVortexAction(),
            FeedType.CABBAGE to PickFeedAction(),
            FeedType.BERRY to UpgradeBerryAction(),
            FeedType.PRUNE to GiveFeedByVortexAction(),
            FeedType.CARROT to AddCloverByCarrotCountAction(),
        )
    }
}

// ============================================================
//  Basic Rules
// ============================================================

class AddGemByVortexAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val count = if (state.ruleset.map.isOnVortex(player.position)) 2 else 1

        return player.incrementGemCount(count)
    }
}

class RollDiceAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player =
        player.rollDice(state, context)
}

class PutBackFeedAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player =
        player.putBackFeed(state, context)
}

class MoveToNextVortexAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val nextPosition = state.ruleset.map.nextVortex(player.position)

        return player.moveToPosition(nextPosition)
    }
}

class MoveByGemAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player =
        player.moveForward(player.gemCount)
}

class AddCloverByVortexAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val count = if (state.ruleset.map.isOnVortex(player.position)) 2 else 1

        return player.incrementCloverCount(count)
    }
}

class SleepAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        player.callback.onSleep(player)

        return if (player.sleepCount <= 1)
            player.incrementSleepCount()
        else
            player.buyFeeds(state, context)
    }
}

// ============================================================
//  Advanced Rules
// ============================================================

class AddGemByPriceAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        return player.incrementGemCount(feed.price)
    }
}

class RollDiceByVortexAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val act = { player: Player -> state.ruleset.dice.roll(context.random).performAction(player, state, context) }

        return if (state.ruleset.map.isOnVortex(player.position))
            act(act(player))
        else
            act(player)
    }
}

class UpgradeBerryAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        // Upgrade the feed to a higher level of berry, or radish.
        val upgradedFeed = Feed.ALL_FEEDS
            .filter { it.type == FeedType.BERRY && it.price > feed.price }
            .minByOrNull { it.price }
            ?: Feed.RADISH

        return player.removeConsumedFeed(feed).addFeed(upgradedFeed)
    }
}

class PickFeedAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player =
        player.pickAndConsumeRandomFeed(state, context)
}

class GiveFeedByVortexAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val targetFeedValue = if (state.ruleset.map.isOnVortex(player.position)) 4 else 2
        val feeds = Feed.ALL_FEEDS.filter { it.value == targetFeedValue }

        return player.chooseAndAddFreeFeed(feeds, state, context)
    }
}

class AddCloverByCarrotCountAction : FeedAction {
    override fun performAction(player: Player, feed: Feed, state: GameState, context: GameContext): Player {
        val carrotCount = player.consumedFeeds.count { it.type == FeedType.CARROT }

        return player.incrementCloverCount(carrotCount)
    }
}