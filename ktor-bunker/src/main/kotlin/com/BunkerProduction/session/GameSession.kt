package com.BunkerProduction.session

@kotlinx.serialization.Serializable
data class GameSession(
    val username: String,
    val sessionID: String,

)
