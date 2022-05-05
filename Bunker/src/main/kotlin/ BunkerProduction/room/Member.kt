package com.example.room

import io.ktor.util.*
import io.ktor.websocket.*

@kotlinx.serialization.Serializable
data class Member(
    val username: String,
    val sessionID: String,
    val socket: WebSocketSession
)
