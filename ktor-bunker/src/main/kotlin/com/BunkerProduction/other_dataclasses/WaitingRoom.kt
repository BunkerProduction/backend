package com.BunkerProduction.other_dataclasses
@kotlinx.serialization.Serializable
data class WaitingRoom(
    var players: List<Player>,
    var sessionID: String
)