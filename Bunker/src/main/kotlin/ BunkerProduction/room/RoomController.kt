package com.example.room

import ` BunkerProduction`.other_dataclasses.Status
import com.example.other_dataclasses.DataSource
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

class RoomController (){
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
    suspend fun IamHere(username: String, sessionID: String)
    {
        members.values.forEach{ member ->
            val status = Status(
                username = username,
                sessionID = sessionID,

            )
            val parsedStatus  = Json.encodeToString(status)
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


