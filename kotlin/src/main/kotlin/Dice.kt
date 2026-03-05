package com.sheepduke.donkeyrace

import kotlin.random.Random

class Dice(val actions: List<DiceAction>) {
    fun roll(random: Random): DiceAction = actions.random(random)

    companion object {
        val WHITE_DICE = Dice(
            listOf(
                MoveAction(1),
                MoveAction(3),
                AddFeedAction(Feed.APPLE2),
                AddFeedAction(Feed.CABBAGE2),
                AddGemAction(1)
            )
        )

        val YELLOW_DICE = Dice(
            listOf(
                MoveAction(0),
                MoveAction(1),
                MoveAction(1),
                MoveAction(2),
                MoveAction(3),
                MoveAction(4),
            )
        )
    }
}