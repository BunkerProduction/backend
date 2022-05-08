package com.BunkerProduction.other_dataclasses

import io.ktor.websocket.*

@kotlinx.serialization.Serializable
data class Status(
    var username: String,
    var sessionID: String,

)

