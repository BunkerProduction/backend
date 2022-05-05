package  BunkerProduction.plugins

import ` BunkerProduction`.di.di
import com.example.room.RoomController
import com.example.routings.connect_to_room
import com.example.routings.create_game
import com.example.routings.gameSocket
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.kodein.di.instance
import org.kodein.di.newInstance

fun Application.configureRouting() {
    val roomController by di.newInstance { RoomController() }

    routing {
        get("/") {
            call.respondText("Hi! it's Bunker")
        }
        connect_to_room()
        create_game()
        gameSocket(roomController)
    }
}
