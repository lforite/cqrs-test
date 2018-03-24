package cqrs

import cqrs.model.{Aggregate, BankAccount, Event, Subscriber}

import scala.collection.mutable.ListBuffer


trait EventBus[T <: Aggregate] {
  def publish(event: Event[T]): Either[String, Unit]

  def subscribe(subscriber: Subscriber[T]): Unit
}

object BankAccountEventBus extends EventBus[BankAccount] {
  private val subscribers: ListBuffer[Subscriber[BankAccount]] = ListBuffer[Subscriber[BankAccount]]()

  def publish(event: Event[BankAccount]): Either[String, Unit] = {
    subscribers.foreach {
      _.consume(event)
    }
    Right(())
  }

  def subscribe(subscriber: Subscriber[BankAccount]): Unit = {
    subscribers += subscriber
  }
}
