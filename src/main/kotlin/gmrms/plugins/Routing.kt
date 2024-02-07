package gmrms.plugins

import gmrms.clientes.ClienteDB
import gmrms.clientes.SaldoException
import gmrms.clientes.Transacao
import gmrms.clientes.getExtrato
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Application.configureRouting() {
    routing {
        get("/clientes/{id}/extrato") {
            val cliente = ClienteDB.findById(id()) ?: return@get notFound()
            ok(cliente.getExtrato())
        }
        post("/clientes/{id}/transacoes"){
            val transacao = recebeTransacao() ?: return@post unprocessableEntity()
            try {
                val cliente = ClienteDB.registraTransacao(id(), transacao) ?: return@post notFound()
                ok(cliente.saldo)
            } catch (e: SaldoException){
                unprocessableEntity()
            }
        }
    }
}

// Os testes "validações" checavam pelo status code 422, em vez de 400
suspend fun PipelineContext<Unit, ApplicationCall>.recebeTransacao() = try {
    call.receive<Transacao>()
} catch (e: BadRequestException){
    null
}

suspend fun PipelineContext<Unit, ApplicationCall>.notFound() =
    call.respond(HttpStatusCode.NotFound)

suspend fun PipelineContext<Unit, ApplicationCall>.unprocessableEntity() =
    call.respond(HttpStatusCode.UnprocessableEntity)

suspend inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.ok(value: T) =
    call.respond(HttpStatusCode.OK, value)

fun PipelineContext<Unit, ApplicationCall>.id(): Int =
    call.parameters["id"]?.toInt() ?: throw BadRequestException("id não informado")