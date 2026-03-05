package com.sheepduke.donkeyrace

interface PlayerStrategy {
    fun chooseFreeFeed(player: Player, feeds: List<Feed>, state: GameState, context: GameContext): Feed

    fun chooseFeedToPutBack(player: Player, state: GameState, context: GameContext): Feed

    fun buyFeeds(player: Player, state: GameState, context: GameContext): List<Feed>
}