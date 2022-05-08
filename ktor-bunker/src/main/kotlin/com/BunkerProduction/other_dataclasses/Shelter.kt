package com.BunkerProduction.other_dataclasses

import java.util.Arrays

@kotlinx.serialization.Serializable

data class Shelter(
    val name: String,
    val icon: String,
    val description: String,
    val conditions: Array<ShelterCondition>
) {
    //----------------------IDE_Code---------------------------------------
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shelter

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (description != other.description) return false
        if (!conditions.contentEquals(other.conditions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + conditions.contentHashCode()
        return result
    }
}
//-------------------------------------------------------------