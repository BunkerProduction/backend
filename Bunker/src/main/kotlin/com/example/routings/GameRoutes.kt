package com.example.routings

import com.example.room.RoomController
import com.example.session.GameSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach

fun Route.Connect_to_room() {
    route("/connect_to_game/{number_room}") {
        get {
            val num_room = call.parameters["number_room"]
            call.respondText("Number room: $num_room")
        }
    }
}

fun Route.Create_game() {
    route("/create_game") {
        put ("{room}"){
            val room = call.parameters["room"] ?: return@put
            call.respondText("Number $room")
        }
        post("/user"){
            val formParameters = call.receiveParameters()
            val username = formParameters["username"].toString()
            call.respondText("The '$username' account is created")
        }
    }
}

fun Route.GameSocket(roomController: RoomController) {
    webSocket("/game-socket") {
        val session = call.sessions.get<GameSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            roomController.onJoin(
                username = session.username,
                sessionID = session.sessionID,
                socket = this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.IamHere(
                        username = session.username,
                        sessionID = session.sessionID,
                        socket = this
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDissconect(session.username)
        }

    }
}




