package com.example.plugins

import com.example.session.GameSession
import io.ktor.util.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<GameSession>("GAME_SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
    if(call.sessions.get<GameSession>() == null) //Проверка были ли уже сессия
    {
        val username = call.parameters["username"] ?: "Player"

        call.sessions.set(GameSession(username, generateNonce())) //generateNonce - генерация идентификатора сеанса
    }
    }
}
