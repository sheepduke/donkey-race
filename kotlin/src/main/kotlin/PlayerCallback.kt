package com.sheepduke.donkeyrace

interface PlayerCallback {
    fun onPickFeed(player: Player, feed: Feed)

    fun onChangePosition(player: Player, position: Position)

    fun onSleep(player: Player)

    fun onBuyFeeds(player: Player, feeds: List<Feed>)

    fun onPerformFeedAction(player: Player, action: FeedAction?)

    fun onPerformDiceAction(player: Player, action: DiceAction)
}