package gmrms.clientes

import clientes
import com.mongodb.MongoCommandException
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.PushOptions
import com.mongodb.client.model.ReturnDocument
import org.litote.kmongo.*

object ClienteDB {

    suspend fun findById(id: Int): Cliente? {
        return clientes.findOne(Cliente::_id eq id)
    }

    suspend fun registraTransacao(id: Int, transacao: Transacao): Cliente? {
        try {
            return clientes.findOneAndUpdate(
                Cliente::_id eq id,
                combine(
                    pushEach(
                        Cliente::ultimas_transacoes,
                        listOf(transacao),
                        PushOptions().position(0).slice(10)
                    ),
                    inc(
                        Cliente::saldo / Saldo::saldo,
                        transacao.valor * transacao.tipo.multiplicador
                    )
                ),
                FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )
        } catch (e: MongoCommandException) {
            throw SaldoException()
        }
    }

}