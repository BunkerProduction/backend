package com.BunkerProduction.room

import com.BunkerProduction.other_dataclasses.Status
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController (){
     private val members = ConcurrentHashMap<String, Player>()

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

    suspend fun IamHere(username: String, sessionID: String, socket: WebSocketSession, text: String, connection: String)
    {
       print(members.values) //Хэш-карта
        members.values.forEach{ member ->
            val status = Status(
                username = username,
                sessionID = sessionID,
                text = text,
                connection = connection
            )

            val player1: Player = Player(
                username = "Tim",
                sessionID = "12313",
                socket = socket
            )
            val player2: Player = Player(
                username = "Kevin",
                sessionID = "12313",
                socket = socket
            )
            val player3: Player = Player(
                username = "Jane",
                sessionID = "12313",
                socket = socket
            )
            val list: MutableList<String> = ArrayList()
            list+=username
//            val ArrayPlayer: Array<Player> = arrayOf(player1, player2)
//            var MapPlayerToMapVotes = mutableMapOf(1 to ArrayPlayer)
//            var MapVotesToArrayPlayers = mutableMapOf(
//                player1 to MapPlayerToMapVotes,
//                player2 to MapPlayerToMapVotes
//            )

//            var parseMapVotesToArrayPlayers = Json.encodeToString(MapVotesToArrayPlayers)
//            var parseMapPlayerToMapVotes = Json.encodeToString(MapPlayerToMapVotes)
            val parsedStatus  = Json.encodeToString(status)
            member.socket.send(Frame.Text(parsedStatus))
//            member.socket.send(Frame.Text(parseMapVotesToArrayPlayers))
//            member.socket.send(Frame.Text(parseMapPlayerToMapVotes))
        }


    }

    suspend fun tryDissconect(username: String){
        members[username]?.socket?.close() // Закрываем сессию для user
        if(members.containsKey(username)){
            members.remove(username) //удаление из хэш карты
        }
    }

}


