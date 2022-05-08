package com.BunkerProduction.plugins

import com.BunkerProduction.session.GameSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<GameSession>("GAME_SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {
        if(call.sessions.get<GameSession>() == null) //Проверка были ли уже сессия
        {
            val username = call.parameters["username"] ?: "Player"

            call.sessions.set(GameSession(username, generateNonce())) //generateNonce - генерация идентификатора сеанса
        }
    }
}