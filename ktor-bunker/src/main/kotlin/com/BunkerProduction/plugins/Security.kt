package com.BunkerProduction.plugins

import com.BunkerProduction.session.GameSession
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.kodein.di.Copy

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<GameSession>("GAME_SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {

        if((call.sessions.get<GameSession>() == null)) //Проверка были ли уже сессия
        {
            val username = call.parameters["username"] ?: "Player"
            val sessionID = call.parameters["sessionID"] ?: "None"
            print(sessionID + username)
            if(sessionID == "None") {
                call.sessions.set(GameSession(username, generateNonce()))
            }//generateNonce - генерация идентификатора сеанса
            else
            {
                call.sessions.set(GameSession(username, sessionID))
            }
        }

        }
    }

