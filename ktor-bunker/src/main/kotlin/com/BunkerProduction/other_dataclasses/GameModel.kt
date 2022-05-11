package com.BunkerProduction.other_dataclasses

import com.BunkerProduction.enums.GameState
import com.BunkerProduction.room.Player

@kotlinx.serialization.Serializable
data class GameModel(
    val preferences: GamePreferences,
    val players: Array<Player>?,
    val gameState: GameState,
    var initialNumberOfPlayers: Int,
    var turn: Int,
    var round: Int,
    var votes: MutableMap<Player, MutableMap<Int, Array<Player>>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameModel

        if (preferences != other.preferences) return false
        if (!players.contentEquals(other.players)) return false
        if (gameState != other.gameState) return false
        if (initialNumberOfPlayers != other.initialNumberOfPlayers) return false
        if (turn != other.turn) return false
        if (round != other.round) return false
        if (votes != other.votes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = preferences.hashCode()
        result = 31 * result + players.contentHashCode()
        result = 31 * result + gameState.hashCode()
        result = 31 * result + initialNumberOfPlayers
        result = 31 * result + turn
        result = 31 * result + round
        result = 31 * result + votes.hashCode()
        return result
    }
}

// other functions
private fun generatePlayers() {} // функция для генерации и присваиванию игрокам их аттрибутов
private fun nextTurn() {}
//     если круг сделан
//    Открыть новый аттрибут бункера
//
//    preferences.shelter.exposeCondition()
//
//     и это не первый начать голосование
//     gameState = .vote
//
//     затем уведомить игрока чей ход сейчас
//     players[turn]
fun playerExposedCard(player: Player, attribute: Attribute) {
    // найти карточку игрока и isExposed = true
    nextTurn() }
