package com.sheepduke.donkeyrace

data class Feed(val name: String, val type: FeedType, val value: Int, val price: Int) {
    init {
        require(value >= 0)
        require(price >= 0)
    }

    companion object {
        val APPLE1 = Feed("apple1", FeedType.APPLE, 1, 1)
        val APPLE2 = Feed("apple2", FeedType.APPLE, 2, 2)
        val APPLE4 = Feed("apple4", FeedType.APPLE, 4, 3)

        val CORN1 = Feed("corn1", FeedType.CORN, 1, 1)
        val CORN2 = Feed("corn2", FeedType.CORN, 2, 2)
        val CORN4 = Feed("corn4", FeedType.CORN, 4, 3)

        val CABBAGE1 = Feed("cabbage1", FeedType.CABBAGE, 1, 1)
        val CABBAGE2 = Feed("cabbage2", FeedType.CABBAGE, 2, 2)
        val CABBAGE4 = Feed("cabbage4", FeedType.CABBAGE, 4, 3)

        val BERRY1 = Feed("berry1", FeedType.BERRY, 1, 1)
        val BERRY2 = Feed("berry2", FeedType.BERRY, 2, 2)
        val BERRY4 = Feed("berry4", FeedType.BERRY, 4, 3)

        val PRUNE = Feed("prune", FeedType.PRUNE, 5, 4)

        val CARROT = Feed("carrot", FeedType.CARROT, 5, 4)

        val RADISH = Feed("radish", FeedType.RADISH, 8, 5)

        val GRASS = Feed("grass", FeedType.GRASS, 0, 0)

        val ALL_FEEDS = listOf(
            APPLE1, APPLE2, APPLE4,
            CORN1, CORN2, CORN4,
            CABBAGE1, CABBAGE2, CABBAGE4,
            BERRY1, BERRY2, BERRY4,
            PRUNE, CARROT, RADISH, GRASS
        )
    }
}