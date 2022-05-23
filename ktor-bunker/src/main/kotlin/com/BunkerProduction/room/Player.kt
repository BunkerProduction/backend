package com.BunkerProduction.room

import io.ktor.websocket.*

@kotlinx.serialization.Serializable
data class Player(
    val id: String,
    val username: String,
    var sessionID: String,
    var socket: WebSocketSession?,
    var isCreator: Boolean
)
