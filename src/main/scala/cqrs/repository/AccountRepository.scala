package cqrs.repository

import cqrs._
import cqrs.model._

object AccountRepository {

  def getAggregate(bankAccountId: BankAccountId)(implicit applyable: Applyable[BankAccount]): Option[BankAccount] = {
    EventStore.retrieve(bankAccountId) match {
      case Nil => None
      case xs => Some(xs.foldLeft(BankAccount.genesis)((acc, e) => applyable.apply(acc, e)))
    }
  }
}
