package com.BunkerProduction.routings

import com.BunkerProduction.other_dataclasses.GamePreferences
import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
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
            send("You are connected!")
            val thisConnection = Connection(this)
            connections += thisConnection

//            send("You've logged in as [${thisConnection.name}]")
//            send("This_connection_session: [${thisConnection.session}]")
//            send("Players_in_Hash_Map_before_connect: [${roomController.get_members()}]")
            val session = call.sessions.get<GameSession>()

            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
                return@webSocket
            }
            try {
                if (session.isCreator == "true") {
                    roomController.onCreateGame(
                        id = session.id,
                        username = session.username,
                        sessionID = session.sessionID,
                        socket = this,
                        gameModel = session.gameModel
                    )
                    send(session.id)
//                   send("gameModel: ${roomController.getgameModels()}")
//                   send("isCreator: ${session.isCreator}")
                }
                if ((session.isCreator == "false") && (roomController.roomisExist(session.sessionID))) {
                    send(session.id)
                    roomController.onJoin(
                        id = session.id,
                        username = session.username,
                        sessionID = session.sessionID,
                        socket = this
                    )
//                    send(Json.encodeToJsonElement(roomController.get_waitingRoom(session.sessionID)).toString())
//                    send("gameModels: ${roomController.getgameModels()}")
//                   send("isCreator : ${session.isCreator}")
//                     send(roomController.getPlayers(session.sessionID).toString())

                }
                if ((session.isCreator == "false") && (!roomController.roomisExist(session.sessionID))) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "This session is not exist"))
                    return@webSocket
//                    send("gameModels: ${roomController.getgameModels()}")
//                    send("isCreator : ${session.isCreator}")
                }
                incoming.consumeEach { frame ->
                    if ((frame is Frame.Text) && (frame.readText()!="waiting_room")&& (frame.readText()!="game")&& (frame.readText()!="game_models")&& (frame.readText()!="clean")) {
                        val input_json = Json.decodeFromString<GamePreferences>(frame.readText())

                        roomController.GetDataShatus(
                            gamePreferences = input_json,
                            sessionID = session.sessionID
                        )
//                        send("gameModels: ${roomController.getgameModels()}")
//                        send("roomisexist: ${roomController.roomisExist(session.sessionID)}")
//                        send("Players_in_Hash_Map_after_connect: [${roomController.get_members()}]")
                    }
                    if ((frame is Frame.Text) && (frame.readText()=="waiting_room"))  {
                        send(Json.encodeToJsonElement(roomController.get_waitingRoom(session.sessionID)).toString())
                    }
                    if ((frame is Frame.Text) && (frame.readText()=="game"))  {
                    send(Json.encodeToJsonElement(roomController.getgameModel(session.sessionID)).toString())
                    }
                    if ((frame is Frame.Text) && (frame.readText()=="game_models"))  {
                        send(Json.encodeToJsonElement(roomController.getgameModels()).toString())
                    }
                    if ((frame is Frame.Text) && (frame.readText()=="clean"))  {
                        send(roomController.clean())
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                roomController.tryDissconect(this, session.sessionID)
                connections.remove(thisConnection)
            }
        }

}








