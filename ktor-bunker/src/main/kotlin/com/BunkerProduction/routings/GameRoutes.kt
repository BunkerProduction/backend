package com.BunkerProduction.routings

import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        var lastId = AtomicInteger(0)
    }

    val name = "user${lastId.getAndIncrement()}"
}

fun Route.gameSocket(roomController: RoomController) {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    webSocket("/game") {
        val thisConnection = Connection(this)
        connections += thisConnection
        send("You've logged in as [${thisConnection.name}]")


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
                        text = frame.readText(),
                        connection = thisConnection.name
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





