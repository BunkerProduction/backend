package com.example.room

import com.example.other_dataclasses.DataSource
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController (

    private val DataSource: DataSource

        ){
     private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionID: String,
        socket: WebSocketSession,

    ) {

        members[username] = Member(
            username = username,
            sessionID = sessionID,
            socket = socket
        )


    }
    suspend fun IamHere(username: String, sessionID: String, socket: WebSocketSession)
    {
        members.values.forEach{ member ->
            val status = Member(
                username = username,
                sessionID = sessionID,
                socket = socket
            )
            val parsedStatus  = Json.encodeToString(Member)
            member.socket.send(Frame.Text(parsedStatus))
        }
    }

    suspend fun tryDissconect(username: String){
        members[username]?.socket?.close() // Закрываем сессию для user
        if(members.containsKey(username)){
            members.remove(username) //удаление из хэш карты
        }
    }

}


