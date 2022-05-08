package com.BunkerProduction.routings

import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.connect_to_room() {
    route("/connect_to_game/{number_room}") {
        get {
            val num_room = call.parameters["number_room"]
            call.respondText("Number room: $num_room")
        }
    }
}

fun Route.create_game() {
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

fun Route.gameSocket(roomController: RoomController) {
    webSocket("/game") {
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
        }
        finally {
            roomController.tryDissconect(session.username)
        }

    }
}





