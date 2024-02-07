import com.mongodb.ConnectionString
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.ValidationOptions
import gmrms.clientes.Cliente
import gmrms.clientes.Saldo
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.expr
import org.litote.kmongo.from
import org.litote.kmongo.projection
import org.litote.kmongo.reactivestreams.KMongo

const val databaseName = "rinhadebackend"
const val collectionName = "cliente"

val primary = System.getenv()["INIT_DB"]
val connectionString = System.getenv()["MONGO_CONNECTION_STRING"] ?: "mongodb://localhost:27017"

val client = KMongo.createClient(ConnectionString(connectionString)).coroutine
val database = client.getDatabase(databaseName)
val clientes = database.getCollection<Cliente>(collectionName)

suspend fun initDatabase(){
    if (primary != "true") {
        println("Pulando inicialização do banco de dados")
        return
    }
    try {
        println("Configurando collection de clientes...")
        database.createCollection("cliente",
            CreateCollectionOptions().validationOptions(
                ValidationOptions().validator(
                    and(
                        expr(
                            "gte".projection from listOf(
                                "\$saldo.saldo", "subtract".projection from listOf(
                                    0, "\$saldo.limite"
                                )
                            )
                        )
                    )
                )
            )
        )
        println("Collection de clientes configurada")
        println("Criando clientes...")
        clientes.insertMany(listOf(
            Cliente(1, Saldo(0, 100000)),
            Cliente(2, Saldo(0, 80000)),
            Cliente(3, Saldo(0, 1000000)),
            Cliente(4, Saldo(0, 10000000)),
            Cliente(5, Saldo(0, 500000)),
        ))
        println("Clientes criados")
    } catch (e: Exception) {
        println("Erro ao configurar collection de clientes: ${e.message}")
    }
}
