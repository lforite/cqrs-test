package cqrs


import scala.collection.mutable.ListBuffer

object EventStore {

  private val eventStore: ListBuffer[Event] = ListBuffer[Event]()

  def store(event: Event): Unit = {
    eventStore += event
  }

  def retrieve(aggregateId: AggregateId): List[Event] = {
    eventStore.toList.filter(_.aggregateId == aggregateId)
  }
}
