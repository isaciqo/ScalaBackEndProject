import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.http.scaladsl.server.Route
import scala.concurrent.ExecutionContextExecutor
import java.util.UUID
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates
import org.mongodb.scala.model.Sorts
import org.mongodb.scala.model.Projections
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._

  object JsonProtocol extends DefaultJsonProtocol {
    implicit val paymentFormat: RootJsonFormat[Payment] =
      jsonFormat4(Payment)
}

object Server extends App {
  val logger = LoggerFactory.getLogger(getClass)
  implicit val system: ActorSystem = ActorSystem("payments-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  import JsonProtocol._
  import MongoConnection._

  val route: Route =
    pathPrefix("payments") {
      concat(

        post {
          entity(as[Payment]) { payment =>
            // Geramos o ID aqui para ser o mesmo em AMBOS os bancos
            val newPayment = payment.copy(
              paymentId = java.util.UUID.randomUUID().toString
            )

            // Criamos os dois Futures
            val saveToSql = PostgresConnection.db.run(PostgresConnection.payments += newPayment)
            val saveToMongo = MongoConnection.collection.insertOne(newPayment).toFuture()

            // Combinamos os dois em um único Future que só completa se ambos derem certo
            val combinedFuture = for {
              _ <- saveToSql
              _ <- saveToMongo
            } yield newPayment

            onComplete(combinedFuture) {
              case scala.util.Success(savedPayment) =>
                logger.info(s"Pagamento ${savedPayment.paymentId} salvo com sucesso no SQL e Mongo")
                complete(savedPayment)

              case scala.util.Failure(e) =>
                // Tratamento de erro detalhado
                logger.error("Falha na persistência dual", e)

                val errorMessage = e match {
                  case ex: org.postgresql.util.PSQLException => "Erro de banco de dados SQL"
                  case ex: com.mongodb.MongoException => "Erro de banco de dados NoSQL"
                  case _ => "Erro interno desconhecido"
                }

                complete(500, s"Falha ao processar pagamento: $errorMessage")
            }
          }
        },

        get {
          parameter("buyerId") { buyerId =>
            val results =
              collection.find(equal("buyerId", buyerId)).toFuture()

            complete(results)
          }
        },

        delete {
          parameter("paymentId") { paymentId =>
            collection.deleteOne(equal("paymentId", paymentId)).toFuture()
            complete("Deleted")
          }
        }

      )
    }

  Http().newServerAt("localhost", 8080).bind(route)
}