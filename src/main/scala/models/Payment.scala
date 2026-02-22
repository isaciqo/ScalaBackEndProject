import java.util.UUID

case class Payment(
                    paymentId: String,
                    buyerId: String,
                    amount: Double,
                    card: String
                  )