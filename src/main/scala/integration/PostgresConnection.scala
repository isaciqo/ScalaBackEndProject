import slick.jdbc.PostgresProfile.api._

object PostgresConnection {
  // Isso aqui lê o arquivo application.conf e abre o POOL de conexões
  // É criado UMA VEZ só durante a vida do app.
  val db = Database.forConfig("postgresDB")

  // Definição da Tabela (Mapeamento da sua Case Class para o SQL)
  class PaymentsTable(tag: Tag) extends Table[Payment](tag, "payments") {
    def paymentId = column[String]("payment_id", O.PrimaryKey)
    def buyerId = column[String]("buyer_id")
    def amount = column[Double]("amount")
    def card = column[String]("card")

    // O * mapeia os campos para a sua Case Class Payment
    def * = (paymentId, buyerId, amount, card) <> (Payment.tupled, Payment.unapply)
  }

  val payments = TableQuery[PaymentsTable]
}