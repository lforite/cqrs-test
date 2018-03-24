package cqrs.model

trait Aggregate {
  def id: AggregateId
}

trait AggregateId {
  def value: String
}

trait Applyable[T <: Aggregate] {
  def apply(aggregate: T, event: Event[T]): T
}

case class BankAccount(
  id: BankAccountId,
  balance: Int
) extends Aggregate

object BankAccount {
  def genesis: BankAccount = BankAccount(BankAccountId(""), 0)

  implicit val applyEvent: Applyable[BankAccount] = new Applyable[BankAccount] {
    override def apply(agg: BankAccount, event: Event[BankAccount]): BankAccount = {
      event match {
        case e: AccountCreatedEvent => BankAccount(e.aggregateId, e.amount)
        case d: MoneyDepositedEvent => agg.copy(balance = agg.balance + d.amount)
        case d: MoneyWithdrewEvent => agg.copy(balance = agg.balance - d.amount)
      }
    }
  }
}

case class BankAccountId(value: String) extends AggregateId