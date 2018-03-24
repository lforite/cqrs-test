package cqrs.handler

import cqrs._
import cqrs.model.{BankAccount, DepositMoneyCommand, MoneyDepositedEvent}
import cqrs.repository.AccountRepository

object DepositMoneyCommandHandler extends CommandHandler[DepositMoneyCommand] {
  def handle(deposit: DepositMoneyCommand): Either[String, MoneyDepositedEvent] = {
    val aggregate = AccountRepository.getAggregate(deposit.id)

    validate(aggregate) match {
      case Left(r) => Left(r)
      case _ => val event = MoneyDepositedEvent(deposit.id, deposit.amount)
        EventStore.store(event)
        Right(event)
    }
  }

  private def validate(bankAccount: Option[BankAccount]): Either[String, Unit] = {
    if (bankAccount.isEmpty)
      Left("Bank accound does not exist")
    else
     Right(())
  }
}
