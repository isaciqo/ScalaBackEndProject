import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._ // Importante para case classes
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

object MongoConnection {

  // 1. Defina o codec específico para a sua case class Payment
  private val paymentCodecRegistry = fromProviders(classOf[Payment])

  // 2. Combine com o registro padrão do MongoDB
  private val codecRegistry = fromRegistries(
    paymentCodecRegistry,
    MongoClient.DEFAULT_CODEC_REGISTRY
  )

  // 3. Passe o registry diretamente nas configurações do Client (Recomendado)
  val settings: MongoClientSettings = MongoClientSettings.builder()
    .applyConnectionString(ConnectionString("mongodb://localhost:27017"))
    .codecRegistry(codecRegistry)
    .build()

  val client: MongoClient = MongoClient(settings)

  val database: MongoDatabase = client.getDatabase("paymentsdb")

  // Agora a coleção já herdará o codec do database/client
  val collection: MongoCollection[Payment] = database.getCollection("payments")
}