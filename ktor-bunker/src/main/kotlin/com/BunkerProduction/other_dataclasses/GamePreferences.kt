package com.BunkerProduction.other_dataclasses

@kotlinx.serialization.Serializable
data class GamePreferences(
    var voitingTime: Int,
    val catastrophe: Catastrophe,
    val shelter: Shelter,
    val difficulty: GameDifficulty
)