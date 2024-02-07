package gmrms.clientes

import io.ktor.server.plugins.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Extrato(
   val saldo: SaldoExtrato,
   val ultimas_transacoes: List<Transacao>
)

@Serializable
data class SaldoExtrato(
    var total: Int = 0,
    var limite: Int = 0,
    var data_extrato: LocalDateTime = now(),
)

@Serializable
data class Cliente(
    val _id: Int = 0,
    val saldo: Saldo = Saldo(),
    val ultimas_transacoes: List<Transacao> = emptyList(),
)

@Serializable
data class Saldo(
    var saldo: Int = 0,
    var limite: Int = 0,
)

@Serializable
data class Transacao(
    val valor: Int,
    val tipo: TipoTransacao,
    val descricao: String,
    val realizada_em: LocalDateTime = now(),
) {
    init {
        if (valor < 0) throw BadRequestException("Valor deve ser maior que zero")
        if (descricao.length !in 1..10) throw BadRequestException("Descrição deve ter entre 1 e 10 caracteres")
    }
}

private fun now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

enum class TipoTransacao(val multiplicador: Int) {
    d(-1),
    c(1)
}