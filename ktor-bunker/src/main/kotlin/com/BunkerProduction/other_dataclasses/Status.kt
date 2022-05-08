package com.BunkerProduction.other_dataclasses

@kotlinx.serialization.Serializable
data class Status(
    var username: String,
    var sessionID: String,
    var text: String

)

