package cqrs

import scala.collection.mutable
import scala.collection.mutable.Set

object AccountProjection extends Subscriber {

  private val accountProjection = new mutable.HashMap[BankAccountId, BankAccount]()
  private val allWithdrewTransactions =
    new mutable.HashMap[BankAccountId, mutable.Set[MoneyWithdrewEvent]]()
      with mutable.MultiMap[BankAccountId, MoneyWithdrewEvent]


  def consume(event: Event): Unit = {
    projectAccount(event)
    projectWithdrewTransaction(event)
  }

  private def projectAccount(event: Event): Unit = {
    event match {
      case AccountCreatedEvent(id, amount) =>
        accountProjection.put(id, BankAccount(id, amount))
      case MoneyDepositedEvent(id, amount) => accountProjection.get(id).foreach { acc =>
        accountProjection.put(id, acc.copy(balance = acc.balance + amount))
      }
      case MoneyWithdrewEvent(id, amount) => accountProjection.get(id).foreach { acc =>
        accountProjection.put(id, acc.copy(balance = acc.balance + amount))
      }
    }
    ()
  }

  private def projectWithdrewTransaction(event: Event): Unit = {
    event match {
      case e: MoneyWithdrewEvent => allWithdrewTransactions.addBinding(e.bankAccountId, e)
      case _ => ()
    }
    ()
  }

  def get(bankAccountId: BankAccountId): Option[BankAccount] = {
    accountProjection.get(bankAccountId)
  }

  def getMoneyWithdrawingProject(bankAccountId: BankAccountId): Option[Set[MoneyWithdrewEvent]] = {
    allWithdrewTransactions.get(bankAccountId)
  }

}
