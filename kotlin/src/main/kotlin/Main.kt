package com.sheepduke.donkeyrace

import com.sheepduke.donkeyrace.player.EasyAiPlayerStrategy
import kotlin.random.Random

class ConsolePlayerCallback : PlayerCallback {
    override fun onPickFeed(player: Player, feed: Feed) {
        println("Player [${player.name}] picked feed ${feed.name}")
    }

    override fun onChangePosition(player: Player, position: Position) {
        if (player.position < position) {
            println("Player [${player.name}] moved to $position")
        }
    }

    override fun onPerformFeedAction(player: Player, action: FeedAction?) {
        println("Player [${player.name}] will $action")
    }

    override fun onPerformDiceAction(player: Player, action: DiceAction) {
        println("Player [${player.name}] rolled $action")
    }

    override fun onSleep(player: Player) {
        println("Player [${player.name}] slept")
    }

    override fun onBuyFeeds(player: Player, feeds: List<Feed>) {
        val feedsString = feeds.joinToString(", ") { it.name }
        println("Player [${player.name}] bought new feeds: $feedsString")
    }
}

fun main() {
    println("Donkey Race!")

    val ruleset = GameRuleset.DEFAULT

    val playerDonkey = Player(
        "donkey", ruleset.initialFeeds,
        strategy = EasyAiPlayerStrategy(),
        callback = ConsolePlayerCallback()
    )

    val playerSheep = Player(
        "sheep", ruleset.initialFeeds,
        strategy = EasyAiPlayerStrategy(),
        callback = ConsolePlayerCallback()
    )

    val context = GameContext(Random)

    var round = 1
    var state = GameState(
        listOf(playerDonkey, playerSheep),
        ruleset
    )

    while (round < 100) {
        println("Round $round")

        val newPlayers = mutableListOf<Player>()

        for (player in state.players) {
            val player = player.pickAndConsumeRandomFeed(state, context)
            newPlayers.add(player)

            if (player.position > ruleset.map.length) {
                println("Player [${player.name}] has won!")

                return
            }
        }

        state = GameState(newPlayers, ruleset)

        round += 1
    }
}
