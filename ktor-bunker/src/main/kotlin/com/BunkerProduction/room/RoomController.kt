package com.BunkerProduction.room

import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.session.GenerateRoomCode
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController () {

    private val members = ConcurrentHashMap<String, Player>()
    fun Exist(sessionID: String): String {
        var sessionIDmoded = sessionID
        if (sessionIDmoded == "None") {
            sessionIDmoded = GenerateRoomCode()
        }
        members.values.forEach { member ->
            while (member.sessionID != sessionID) {
                sessionIDmoded = GenerateRoomCode()
            }
        }
            return sessionIDmoded
    }
    fun onCreateGame(
        username: String,
        sessionID: String,
        socket: WebSocketSession,
        gameModel: GameModel
    )
    {

        members[username] = Player(
            username = username,
            sessionID = sessionID,
            socket = socket
        )
    }
    fun onJoin(
        username: String,
        sessionID: String,
        socket: WebSocketSession,
    ) {

        members[username] = Player(
            username = username,
            sessionID = sessionID,
            socket = socket
        )

    }

    suspend fun IamHere(username: String, sessionID: String, socket: WebSocketSession, text: String, connection: String, isCreator: Boolean)
    {
       print(members.values) //Хэш-карта

        members.values.forEach{ member ->
            val status = Status(
                username = username,
                sessionID = sessionID,
                text = text,
                connection = connection,
                isCreator = isCreator
            )

            val parsedStatus  = Json.encodeToString(status)
            member.socket?.send(Frame.Text(parsedStatus))
        }
    }

    suspend fun tryDissconect(username: String){
        members[username]?.socket?.close() // Закрываем сессию для user
        if(members.containsKey(username)){
            members.remove(username) //удаление из хэш карты
        }
    }



}



