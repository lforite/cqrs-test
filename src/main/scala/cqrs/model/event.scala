package cqrs.model

import java.time.Instant


sealed trait Event[T <: Aggregate] {
  def aggregateId: AggregateId
}

case class EventMetaData(createdAt: Instant)
object EventMetaData {
  def apply(): EventMetaData = EventMetaData(Instant.now())
}

case class AccountCreatedEvent(aggregateId: BankAccountId, amount: Int) extends Event[BankAccount]
case class MoneyWithdrewEvent(aggregateId: BankAccountId, amount: Int) extends Event[BankAccount]
case class MoneyDepositedEvent(aggregateId: BankAccountId, amount: Int) extends Event[BankAccount]

trait Subscriber[T <: Aggregate] {
  def consume(event: Event[T]): Unit
}