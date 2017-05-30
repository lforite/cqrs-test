package cqrs

import scala.collection.mutable.ListBuffer

object EventBus {

  private val subscribers: ListBuffer[Subscriber] = ListBuffer[Subscriber]()

  def publish(event: Event): Either[String, Unit] = {
    subscribers.foreach {
      _.consume(event)
    }
    Right(())
  }

  def subscribe(subscriber: Subscriber): Unit = {
    subscribers += subscriber
  }
}
