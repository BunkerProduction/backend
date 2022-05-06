package  BunkerProduction

import BunkerProduction.plugins.configureRouting
import BunkerProduction.plugins.configureSecurity
import BunkerProduction.plugins.configureSerialization
import BunkerProduction.plugins.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    configureSockets()
    configureRouting()
    configureSerialization()
    configureSecurity()
}
