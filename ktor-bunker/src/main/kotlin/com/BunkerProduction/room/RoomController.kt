package com.BunkerProduction.room

import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.session.GenerateRoomCode
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController () {

     private val members = ConcurrentHashMap<String, Player>()
     private val gamemodel = ConcurrentHashMap<String, GameModel>()

    fun getgameModels(): MutableList<GameModel> { return gamemodel.values.toMutableList() }
    fun get_members() : MutableCollection<Player> { return members.values}
    fun roomisExist(sessionID: String): Boolean {return gamemodel.containsKey(sessionID)}
    fun getPlayers(sessionID: String): MutableList<Player> {
        var PlayersList: MutableList<Player> = mutableListOf()
            members.values.forEach { member ->
            if (member.sessionID == sessionID) {
                PlayersList.add(member)
            }
        }
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
        members[username] = Player(
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

        members[username]?.let { gamemodel[sessionID]?.players?.add(it) }



    }

    suspend fun IamHere(username: String, sessionID: String, socket: WebSocketSession, gameModel: GameModel, text: String, connection: String, isCreator: Boolean)
    {
//     print(members.values.toMutableList()) //Хэш-карта играков
//        print(gamemodel.values) //Хэш-карта игровых моделей

        members.values.forEach{ member ->
            val status = Status(
                username = username,
                sessionID = sessionID,
                text = text,
                connection = connection,
                isCreator = isCreator,
                gameModel = gameModel
            )

            val parsedStatus  = Json.encodeToString(status)
            member.socket?.send(Frame.Text(parsedStatus))
        }
    }

    suspend fun tryDissconect(username: String, sessionID: String){
        members[username]?.socket?.close() // Закрываем сессию для user
        if(members.containsKey(username)){
            members.remove(username) //удаление из хэш карты Members
        }
        gamemodel[sessionID]?.players = getPlayers(sessionID) // Обновление модели игры
        if (gamemodel[sessionID]?.players?.isEmpty() == true)
        {
            gamemodel.remove(sessionID)
        }
    }

}



