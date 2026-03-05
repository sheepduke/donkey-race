package com.sheepduke.donkeyrace.player

import com.sheepduke.donkeyrace.*

class EasyAiPlayerStrategy : PlayerStrategy {
    override fun chooseFreeFeed(
        player: Player,
        feeds: List<Feed>,
        state: GameState,
        context: GameContext
    ): Feed =
        feeds.random(context.random)

    override fun chooseFeedToPutBack(
        player: Player,
        state: GameState,
        context: GameContext
    ): Feed =
        player.consumedFeeds.minBy { it.price }

    override fun buyFeeds(
        player: Player,
        state: GameState,
        context: GameContext
    ): List<Feed> {
        var gemCount = player.gemCount
        val shop = state.ruleset.shop.sortedByDescending { it.value }
        val shoppingCart = mutableListOf<Feed>()

        for (feed in shop) {
            if (gemCount <= 0 && shoppingCart.size >= 4) {
                break
            }

            if (gemCount >= feed.price && shoppingCart.none { it.type == feed.type }
                && (feed.price == 5
                        || feed.type == FeedType.BERRY
                        || feed.type == FeedType.CORN
                        || feed.type == FeedType.APPLE)) {
                shoppingCart.add(feed)
            }
        }

        return shoppingCart
    }
}