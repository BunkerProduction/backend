package com.BunkerProduction.other_dataclasses

import com.BunkerProduction.enums.GameState
import com.BunkerProduction.room.Player

@kotlinx.serialization.Serializable
data class GameModel(
    var sessionID: String,
    var preferences: GamePreferences,
    var players: MutableList<com.BunkerProduction.other_dataclasses.Player>?,
    val gameState: GameState,
    var initialNumberOfPlayers: Int,
    var turn: Int,
    var round: Int,
//    var votes: MutableMap<Player, MutableMap<Int, Array<Player>>>
)
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
