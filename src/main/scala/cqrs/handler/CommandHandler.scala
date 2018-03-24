package cqrs.handler

import cqrs.BankAccountEventBus
import cqrs.model._

trait CommandHandler[T <: Command] {
  def handle(command: T): Either[String, Event[T#AggregateType]]
}

object AccountCommandHandler extends CommandHandler[AccountCommand] {
  def handle(command: AccountCommand): Either[String, Event[BankAccount]] = {
    val result = command match {
      case d: DepositMoneyCommand => DepositMoneyCommandHandler.handle(d)
      case c: WithdrawMoneyCommand => WithdrawMoneyCommandHandler.handle(c)
      case c: CreateAccountCommand => CreateAccountCommandHandler.handle(c)
    }

    for {
      r         <- result
      published <- BankAccountEventBus.publish(r)
    } yield r
  }
}