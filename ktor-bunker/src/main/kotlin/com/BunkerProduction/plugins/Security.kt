package com.BunkerProduction.plugins

import com.BunkerProduction.di.di
import com.BunkerProduction.enums.GameState
import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import org.kodein.di.newInstance


fun Application.configureSecurity() {
    val roomController by di.newInstance { RoomController() }
    install(Sessions) {
        cookie<GameSession>("GAME_SESSION")
    }
    intercept(ApplicationCallPipeline.Plugins) {

        if((call.sessions.get<GameSession>() == null)) //Проверка были ли уже сессии
        {
            var username = call.parameters["username"] ?: "Player"
            var sessionID = call.parameters["sessionID"] ?: "None"
            var isCreator = call.parameters["isCreator"] ?: "true"
            var voitingTime = call.parameters["voitingTime"] ?: 0
            var initialNumberOfPlayers = call.parameters["initialNumberOfPlayers"] ?: 0
            var turn = call.parameters["turn"] ?: 0
            var round = call.parameters["round"] ?: 0

            var gamePreferences = GamePreferences(
                voitingTime = 4,
                catastropheId = 1,
                shelterId = 2,
                difficultyId = 3
            )
            var gameState = GameState.normal //по умолчанию стоит normal

//            val player1: Player = Player(
//                username = "Tim",
//                sessionID = "12313",
//                socket = null
//            )
//            val player2: Player = Player(
//                username = "Kevin",
//                sessionID = "12313",
//                socket = null
//            )
//            var ArrayPlayer: Array<Player> = arrayOf(player1, player2)
//            var MapPlayerToMapVotes = mutableMapOf(1 to ArrayPlayer)
//            var MapVotesToArrayPlayers = mutableMapOf(
//                player1 to MapPlayerToMapVotes,
//                player2 to MapPlayerToMapVotes)

            if((sessionID == "None")&&(isCreator == "true")) {
                sessionID = roomController.isExist(sessionID)

                var gameModel = GameModel(
                    sessionID = sessionID,
                    preferences = gamePreferences,
                    players = null,
                    gameState = gameState,
                    initialNumberOfPlayers = initialNumberOfPlayers as Int,
                    turn = turn as Int,
                    round = round as Int,
//                votes = MapVotesToArrayPlayers
                )
                call.sessions.set(GameSession(username, sessionID, isCreator, gameModel))
            }
            if((sessionID != "None")&&(isCreator == "false")) {
                var gameModel = GameModel(
                    sessionID = sessionID,
                    preferences = gamePreferences,
                    players = null,
                    gameState = gameState,
                    initialNumberOfPlayers = initialNumberOfPlayers as Int,
                    turn = turn as Int,
                    round = round as Int,
//                votes = MapVotesToArrayPlayers
                )
                call.sessions.set(GameSession(username, sessionID, isCreator, gameModel))
            }

        }

        }
    }

