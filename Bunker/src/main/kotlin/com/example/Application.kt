package com.example

import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

fun main() {
//  embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

    }.start(wait = true)

}


@Suppress("unused")
fun Application.module()
{
    install(Koin)
    
    configureRouting()
    configureSecurity()
    configureSockets()
    configureSerialization()



}




