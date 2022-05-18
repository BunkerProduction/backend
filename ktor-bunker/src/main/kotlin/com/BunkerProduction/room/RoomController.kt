package com.BunkerProduction.room

import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.session.GenerateRoomCode
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class RoomController () {

     private val members = ConcurrentHashMap<String, Player>()
     private val gamemodel = ConcurrentHashMap<String, GameModel>()
    fun getgameModels(): MutableList<GameModel> { return gamemodel.values.toMutableList() }
    fun clean(): String { gamemodel.values.clear(); members.values.clear(); return "Success clean"}
    fun getgameModel(sessionID: String): String { return (gamemodel.values.filter { gameModel -> gameModel.sessionID == sessionID }).toString()}
    fun get_members() : MutableCollection<Player> { return members.values}
    fun roomisExist(sessionID: String): Boolean {return gamemodel.containsKey(sessionID)}

    suspend fun get_waitingRoom(sessionID: String){
        members.values.forEach{ member ->
            if(member.sessionID == sessionID)
                member.socket?.send(Frame.Text((getPlayers(sessionID)).toString()))
        }
    }

    fun getPlayers(sessionID: String): MutableList<Player> {
        var PlayersList = mutableListOf<Player>()
//            members.values.forEach { member ->
//                if (member.sessionID == sessionID) {
//                    PlayersList.add(member)
//                }
//            }
        val res = (members.values.filter { player -> player.sessionID == sessionID }).toMutableList()
        PlayersList = res
        return PlayersList
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
            username = username,
            sessionID = sessionID,
            socket = socket
        )
        gamemodel[sessionID] = GameModel(
            sessionID = sessionID,
            preferences = gameModel.preferences,
            players = getPlayers(sessionID), //Берет всех играков из сессии, хотя она только что создалась (не нужно )
            gameState = gameModel.gameState,
            initialNumberOfPlayers = gameModel.initialNumberOfPlayers,
            turn = gameModel.turn,
            round = gameModel.round
        )

    }
    suspend fun onJoin(
        username: String,
        sessionID: String,
        socket: WebSocketSession,
    ) {
       members[socket.toString()] = Player(
            username = username,
            sessionID = sessionID,
            socket = socket
        )
        members[socket.toString()]?.let { gamemodel[sessionID]?.players?.add(it) }
    }

    suspend fun GetDataShatus(gamePreferences: GamePreferences, sessionID: String)
    {
//     print(members.values.toMutableList()) //Хэш-карта играков
//        print(gamemodel.values) //Хэш-карта игровых моделей
        members.values.forEach{ member ->
            gamemodel[sessionID]?.preferences = gamePreferences
            if(member.sessionID == sessionID)
            member.socket?.send(Frame.Text((getPlayers(sessionID)).toString()))
        }
    }

    suspend fun tryDissconect(socket: DefaultWebSocketServerSession, sessionID: String){
        if(members.containsKey(socket.toString())){
            members.remove(socket.toString()) //удаление из хэш карты Members
            members[socket.toString()]?.socket?.close() // Закрываем сессию для user

            members.values.forEach{ member ->
                if(member.sessionID == sessionID)
                member.socket?.send(Frame.Text((getPlayers(sessionID)).toString()))
            }
        }
        gamemodel[sessionID]?.players = getPlayers(sessionID) // Обновление модели игры
        if (gamemodel[sessionID]?.players?.isEmpty() == true)
        {
            gamemodel.remove(sessionID)
        }
    }

}



