package com.BunkerProduction.room

import io.ktor.websocket.*

@kotlinx.serialization.Serializable
data class Player(
    val username: String,
    val sessionID: String,
    val socket: WebSocketSession?,
)
