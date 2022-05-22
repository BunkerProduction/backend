package com.BunkerProduction.other_dataclasses
@kotlinx.serialization.Serializable
data class Player(
    var isCreator: Boolean,
    var username: String
)