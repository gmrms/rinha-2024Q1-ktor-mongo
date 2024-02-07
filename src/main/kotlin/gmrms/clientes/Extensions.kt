package gmrms.clientes

fun Saldo.getSaldoExtrato() = SaldoExtrato(
    this.saldo,
    this.limite
)

fun Cliente.getExtrato() = Extrato(
    this.saldo.getSaldoExtrato(),
    this.ultimas_transacoes
)