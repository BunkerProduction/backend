package com.BunkerProduction.other_dataclasses

import com.BunkerProduction.Maps.MapOfVotes
import com.BunkerProduction.enums.GameState
import com.BunkerProduction.room.Player
import java.util.Arrays

data class GameModel(
    val preferences: GamePreferences,
    val players: Array<Player>,
    val gameState: GameState,
    var initialNumberOfPlayers: Int,
    var turn: Int,
    var round: Int,
    var votes: MapOfVotes
) {
    //----------------------IDE_Code---------------------------------------
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameModel

        if (preferences != other.preferences) return false
        if (!players.contentEquals(other.players)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = preferences.hashCode()
        result = 31 * result + players.contentHashCode()
        return result
    }
}
//----------------------IDE_Code---------------------------------------