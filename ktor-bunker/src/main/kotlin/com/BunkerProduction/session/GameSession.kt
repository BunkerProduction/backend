package com.BunkerProduction.session

import com.BunkerProduction.other_dataclasses.GameModel

@kotlinx.serialization.Serializable
data class GameSession(
    val username: String,
    val sessionID: String,
    val isCreator: String,
    var gameModel: GameModel
)
