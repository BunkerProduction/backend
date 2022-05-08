package com.BunkerProduction.routings

import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

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
                if ((frame is Frame.Text)){
                    roomController.IamHere(
                        username = session.username,
                        sessionID = session.sessionID,
                        socket = this,
                        text = frame.readText()
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





