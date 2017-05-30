package cqrs

import java.time.Instant

case class BankAccountId(value: String) extends AnyVal

case class BankAccount(id: BankAccountId, balance: Int)

sealed trait Command
case class CreateAccountCommand(initialAmount: Int) extends Command
case class WithdrawMoneyCommand(id: BankAccountId, amount: Int) extends Command
case class DepositMoneyCommand(id: BankAccountId, amount: Int) extends Command

case class AggregateId(value: String) extends AnyVal
case class EventMetaData(createdAt: Instant)
object EventMetaData {
  def apply(): EventMetaData = EventMetaData(Instant.now())
}

sealed trait Event {
  def aggregateId: AggregateId
}

case class AccountCreatedEvent(bankAccountId: BankAccountId, amount: Int) extends Event {
  override def aggregateId: AggregateId = AggregateId(bankAccountId.value)
}

case class MoneyWithdrewEvent(bankAccountId: BankAccountId, amount: Int) extends Event {
  override def aggregateId: AggregateId = AggregateId(bankAccountId.value)
}

case class MoneyDepositedEvent(bankAccountId: BankAccountId, amount: Int) extends Event {
  override def aggregateId: AggregateId = AggregateId(bankAccountId.value)
}

trait Subscriber {
  def consume(event: Event): Unit
}