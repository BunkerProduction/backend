package com.BunkerProduction.room

@kotlinx.serialization.Serializable
class MemberAlreadyExistsException: Exception (
    "Участник с таким именем есть"
)