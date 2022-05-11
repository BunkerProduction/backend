package com.BunkerProduction.plugins

import com.BunkerProduction.room.Player
import com.BunkerProduction.room.RoomController
import com.BunkerProduction.session.GameSession
import com.BunkerProduction.session.GenerateRoomCode
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap


fun Application.configureSecurity() {
    install(Sessions) {
        cookie<GameSession>("GAME_SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {

        if((call.sessions.get<GameSession>() == null)) //Проверка были ли уже сессии
        {
            val username = call.parameters["username"] ?: "Player"
            var sessionID = call.parameters["sessionID"] ?: "None"
//            print(sessionID + username)
            if(sessionID == "None") {

                    sessionID = GenerateRoomCode()

                    call.sessions.set(GameSession(username, sessionID))
            }//generateNonce - генерация идентификатора сеанса
            else
            {
                call.sessions.set(GameSession(username, sessionID))
            }
        }

        }
    }

