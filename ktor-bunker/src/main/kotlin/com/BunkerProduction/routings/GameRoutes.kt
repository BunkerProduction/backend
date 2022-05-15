package com.BunkerProduction.routings

import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

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
        send("This_connection_session: [${thisConnection.session}]")
        send("Players_in_Hash_Map: [${roomController.get_members()}]")
        val session = call.sessions.get<GameSession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {

            if(session.isCreator == "true") {
                roomController.onCreateGame(
                    username = session.username,
                    sessionID = session.sessionID,
                    socket = this,
                    gameModel = session.gameModel
                )
                send("gameModel: ${roomController.getgameModels()}")
                send("is : ${session.isCreator}")
            }
            if(session.isCreator == "false") {
                roomController.onJoin(
                    username = session.username,
                    sessionID = session.sessionID,
                    socket = this
                )
                send("gameModels: ${roomController.getgameModels()}")
                send("is : ${session.isCreator}")
            }

            incoming.consumeEach { frame ->
                if ((frame is Frame.Text)) {
                    roomController.IamHere(
                        username = session.username,
                        sessionID = session.sessionID,
                        socket = this,
                        text = frame.readText(),
                        connection = thisConnection.name,
                        isCreator = session.isCreator.toBoolean(),
                        gameModel = session.gameModel
                    )
                }

                send("gameModels: ${roomController.getgameModels()}")
                send("connections_list: [${connections}]")
                send("Players_in_Hash_Map: [${roomController.get_members()}]")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            roomController.tryDissconect(session.username, session.sessionID)
            connections.remove(thisConnection)
        }

    }

}





