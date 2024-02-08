package gmrms

import gmrms.plugins.configureRouting
import gmrms.plugins.configureSerialization
import initDatabase
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

suspend fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    initDatabase()
    embeddedServer(CIO, port = port, host = "0.0.0.0"){
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
