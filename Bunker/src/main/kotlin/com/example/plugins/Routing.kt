package com.example.plugins

import com.example.di.di
import com.example.room.RoomController
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import com.example.di.mainModule
import com.example.routings.*
import io.ktor.util.reflect.*
import org.kodein.di.instance
import org.kodein.di.newInstance

fun Application.configureRouting() {
    routing() {
        get("/") {
            call.respondText("Hi, it's Bunker!")
        }

    }
    val roomController by di.newInstance { RoomController(instance()) }
    install(Routing) {
        connect_to_room()
        create_game()
        gameSocket(roomController)
    }

}
