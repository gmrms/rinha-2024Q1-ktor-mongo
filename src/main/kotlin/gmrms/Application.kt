package gmrms

import gmrms.plugins.configureRouting
import gmrms.plugins.configureSerialization
import initDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

suspend fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    initDatabase()
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
