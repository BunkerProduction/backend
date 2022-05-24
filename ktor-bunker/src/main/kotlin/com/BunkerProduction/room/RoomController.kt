package com.BunkerProduction.room

import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.session.GenerateRoomCode
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.util.concurrent.ConcurrentHashMap

class RoomController () {

     private val members = ConcurrentHashMap<String, Player>()
     private val gamemodel = ConcurrentHashMap<String, GameModel>()
    fun getgameModels(): MutableList<GameModel> { return gamemodel.values.toMutableList() }
    fun clean(): String { gamemodel.values.clear(); members.values.clear(); return "Success clean"}
    fun getgameModel(sessionID: String): List<GameModel> { return (gamemodel.values.filter { gameModel -> gameModel.sessionID == sessionID })}
    fun get_members() : MutableCollection<Player> { return members.values}
    fun roomisExist(sessionID: String): Boolean {return gamemodel.containsKey(sessionID)}

    fun get_waitingRoom(sessionID: String): WaitingRoom {
        var players = mutableListOf<com.BunkerProduction.other_dataclasses.Player>()
        members.values.forEach{ member ->
            if(member.sessionID == sessionID)
                players += Player(
                    id = generateNonce(),
                    username = member.username,
                    isCreator = member.isCreator
                )
        }
        var waitingRoom = WaitingRoom(
            players = players,
            sessionID = sessionID
        )
        return waitingRoom
    }
    fun getPlayers_NONEsocket(sessionID: String): MutableList<com.BunkerProduction.other_dataclasses.Player> {
        var players = mutableListOf<com.BunkerProduction.other_dataclasses.Player>()
        members.values.forEach{ member ->
            if(member.sessionID == sessionID) {
                players += Player(
                    id = generateNonce(),
                    username = member.username,
                    isCreator = member.isCreator
                )
            }
        }
        return players
    }
    fun getPlayers(sessionID: String): MutableList<Player> {
        val res = (members.values.filter { player -> player.sessionID == sessionID }).toMutableList()
        return res
    }

    fun isExist(sessionID: String): String { // Для нового подключения
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
        members[socket.toString()] = Player(
            id = generateNonce(),
            username = username,
            sessionID = sessionID,
            socket = socket,
            isCreator = true
        )
        gamemodel[sessionID] = GameModel(
            sessionID = sessionID,
            preferences = gameModel.preferences,
            players = getPlayers_NONEsocket(sessionID), //Берет всех играков из сессии, хотя она только что создалась (не нужно )
            gameState = gameModel.gameState,
            initialNumberOfPlayers = gameModel.initialNumberOfPlayers,
            turn = gameModel.turn,
            round = gameModel.round
        )

    }
    suspend fun onJoin(
        id: String,
        username: String,
        sessionID: String,
        socket: WebSocketSession,
    ) {
       members[socket.toString()] = Player(
           id = id,
            username = username,
            sessionID = sessionID,
            socket = socket,
           isCreator = false
        )
        var player_NONEsocket = Player(
                id = generateNonce(),
                isCreator = false,
                username = username
                )

        gamemodel[sessionID]?.initialNumberOfPlayers = gamemodel[sessionID]?.initialNumberOfPlayers!! + 1
        gamemodel[sessionID]?.players?.add(player_NONEsocket)

        members.values.forEach{ member ->
            if(member.sessionID == sessionID)
                member.socket?.send(Frame.Text(Json.encodeToJsonElement(get_waitingRoom(sessionID)).toString()))
        }
    }

    suspend fun GetDataShatus(gamePreferences: GamePreferences, sessionID: String)
    {
//     print(members.values.toMutableList()) //Хэш-карта играков
//        print(gamemodel.values) //Хэш-карта игровых моделей
        members.values.forEach{ member ->
            gamemodel[sessionID]?.preferences = gamePreferences
            if(member.sessionID == sessionID)
            member.socket?.send(Frame.Text(Json.encodeToJsonElement(get_waitingRoom(sessionID)).toString()))
        }
    }

    suspend fun tryDissconect(socket: DefaultWebSocketServerSession, sessionID: String){
        var trig = 0
        if(members.containsKey(socket.toString())){

            members.remove(socket.toString()) //удаление из хэш карты Members
            members[socket.toString()]?.socket?.close() // Закрываем сессию для user
        }

        val hosts = members.values.filter { player -> player.isCreator } //проверка на хоста в комнате

        if(hosts.isEmpty())
        {
            members.values.forEach{ member ->
                if((member.sessionID == sessionID)&&(trig == 0)) {
                    trig++
                    member.isCreator = true //Заменяем хоста
                }

            }
        }

        members.values.forEach{ member ->
            if(member.sessionID == sessionID)
                member.socket?.send(Frame.Text(Json.encodeToJsonElement(get_waitingRoom(sessionID)).toString()))
        }
        gamemodel[sessionID]?.initialNumberOfPlayers = gamemodel[sessionID]?.initialNumberOfPlayers!! - 1
        gamemodel[sessionID]?.players = getPlayers_NONEsocket(sessionID) // Обновление модели игры
        if (gamemodel[sessionID]?.players?.isEmpty() == true)
        {
            gamemodel.remove(sessionID)
        }
    }

}




