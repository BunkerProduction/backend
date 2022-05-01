package com.example.plugins

import com.example.room.RoomController
import com.example.routings.Connect_to_room
import com.example.routings.Create_game
import com.example.routings.GameSocket
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing() {
        get("/") {
            call.respondText("Hi, it's Bunker!")
        }

    }
    val roomController by inject
    install(Routing) {
        Connect_to_room()
        Create_game()
        GameSocket(roomController)
    }

}
