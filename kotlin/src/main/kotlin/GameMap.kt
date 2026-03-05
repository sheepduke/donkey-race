package com.sheepduke.donkeyrace

typealias Position = Int

data class GameMap(val length: Position, val vortexes: List<Position>) {
    fun isOnVortex(position: Position): Boolean = position in vortexes

    fun isEndReached(position: Position): Boolean = position >= length

    fun nextVortex(position: Position): Position = vortexes.find { it > position } ?: position

    companion object {
        val SHORT_MAP: GameMap = GameMap(50, listOf(2, 4, 7, 11, 14, 17, 21, 24, 27, 30, 34, 36, 39, 42, 44, 47))
    }
}