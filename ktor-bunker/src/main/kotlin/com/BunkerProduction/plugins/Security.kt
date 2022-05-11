package com.BunkerProduction.plugins

import com.BunkerProduction.di.di
import com.BunkerProduction.enums.GameState
import com.BunkerProduction.other_dataclasses.*
import com.BunkerProduction.room.Player
import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.websocket.*
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

            val catastrophe = Catastrophe(
                name = "Beercalypse",
                icon = "🍺",
                shortDescription = "Aliens started clonin beer.",
                fullDescription = "You arrived"
                    )

            val shelterCondition = ShelterCondition(
                isExposed = false,
                icon = "🤢",
                name = "First aid kit",
                description = "There is a first aid kit, rubber gloves, masks and a defibrillator at the entrance."
            )

            val shelter = Shelter(
                name = "School Toilet",
                icon = "🤢",
                description = "The toilet is in the basement of your local high school. Even rats die from the smell.",
                conditions = shelterCondition
            )

            val difficulty = GameDifficulty(
                name = "Рандом",
                icon = "📦",
                description = "Уровень сложности выбираеся рандомно"
            )

            var gamePreferences = GamePreferences(
                voitingTime = voitingTime as Int,
                catastrophe = catastrophe,
                shelter = shelter,
                difficulty = difficulty
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

            var gameModel = GameModel(
                preferences = gamePreferences,
                players = null,
                gameState = gameState,
                initialNumberOfPlayers = initialNumberOfPlayers as Int,
                turn = turn as Int,
                round = round as Int,
//                votes = MapVotesToArrayPlayers
            )

            if(sessionID == "None") {
                sessionID = roomController.Exist(sessionID)
                call.sessions.set(GameSession(username, sessionID, isCreator, gameModel))
            }//generateNonce - генерация идентификатора сеанса
            else
            {
                call.sessions.set(GameSession(username, sessionID, isCreator, gameModel))
            }
        }

        }
    }

