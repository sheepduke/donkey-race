package com.sheepduke.donkeyrace

sealed interface DiceAction {
    fun performAction(player: Player, state: GameState, context: GameContext): Player
}

class MoveAction(val step: Int) : DiceAction {
    override fun performAction(player: Player, state: GameState, context: GameContext): Player {
        return player.moveForward(step)
    }
}

class AddGemAction(val count: Int) : DiceAction {
    override fun performAction(player: Player, state: GameState, context: GameContext): Player {
        return player.incrementGemCount(count)
    }
}

class AddFeedAction(val feed: Feed) : DiceAction {
    override fun performAction(player: Player, state: GameState, context: GameContext): Player {
        return player.addFeed(feed)
    }
}