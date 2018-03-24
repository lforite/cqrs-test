package cqrs

import cqrs.model.{Aggregate, AggregateId, BankAccount, Event}

import scala.collection.mutable.ListBuffer

trait EventStore[T <: Aggregate] {
  def store(event: Event[T]): Unit
  def retrieve(aggregateId: AggregateId): List[Event[T]]
}

object EventStore extends EventStore[BankAccount] {

  private val eventStore: ListBuffer[Event[BankAccount]] = ListBuffer[Event[BankAccount]]()

  def store(event: Event[BankAccount]): Unit = {
    eventStore += event
  }

  def retrieve(aggregateId: AggregateId): List[Event[BankAccount]] = {
    eventStore.toList.filter(_.aggregateId == aggregateId)
  }
}
