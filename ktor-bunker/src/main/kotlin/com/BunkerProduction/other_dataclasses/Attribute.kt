package com.BunkerProduction.other_dataclasses

@kotlinx.serialization.Serializable
data class Attribute(
    var isExposed: Boolean,
    val  icon: String,
    val category: String,
    val description: String
)
