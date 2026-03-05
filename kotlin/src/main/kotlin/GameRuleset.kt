package com.sheepduke.donkeyrace

data class GameRuleset(
    val map: GameMap,
    val initialFeeds: List<Feed>,
    val shop: List<Feed>,
    val dice: Dice,
    val actions: Map<FeedType, FeedAction>
) {
    companion object {
        val DEFAULT_INITIAL_FEEDS: List<Feed> = listOf(
            Feed.APPLE1, Feed.APPLE1, Feed.APPLE2, Feed.CORN1, Feed.GRASS, Feed.GRASS, Feed.GRASS, Feed.GRASS
        )

        val PRIMARY_FEEDS: List<Feed> = listOf(
            Feed.APPLE1, Feed.APPLE2, Feed.APPLE4,
            Feed.CORN1, Feed.CORN2, Feed.CORN4,
            Feed.CABBAGE1, Feed.CABBAGE2, Feed.CABBAGE4,
            Feed.BERRY1, Feed.BERRY2, Feed.BERRY4,
            Feed.RADISH
        )

        val EXTENDED_FEEDS: List<Feed> = PRIMARY_FEEDS + listOf(Feed.PRUNE, Feed.CARROT)

        val DEFAULT: GameRuleset = GameRuleset(
            map = GameMap.SHORT_MAP,
            initialFeeds = DEFAULT_INITIAL_FEEDS,
            shop = EXTENDED_FEEDS,
            dice = Dice.WHITE_DICE,
            actions = FeedAction.BASIC_ACTIONS
        )
    }
}