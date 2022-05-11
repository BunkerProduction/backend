package com.BunkerProduction

import com.BunkerProduction.plugins.configureRouting
import com.BunkerProduction.plugins.configureSecurity
import com.BunkerProduction.plugins.configureSerialization
import com.BunkerProduction.plugins.configureSockets
import com.BunkerProduction.room.RoomController
import io.ktor.server.application.*
import org.kodein.di.newInstance

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSockets()
    configureRouting()
    configureSerialization()
    configureSecurity()

}
