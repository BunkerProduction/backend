package com.BunkerProduction.room

@kotlinx.serialization.Serializable
class PlayerAlreadyExistsException: Exception (
    "Участник с таким именем есть"
)