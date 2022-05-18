package com.BunkerProduction.other_dataclasses

@kotlinx.serialization.Serializable
data class GamePreferences(
    var voitingTime: Int,
    val catastropheId: Int,
    val shelterId: Int,
    val difficultyId: Int
)