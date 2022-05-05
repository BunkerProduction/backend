package com.example.session

@kotlinx.serialization.Serializable
data class GameSession(
    val username: String,
    val sessionID: String,

)
