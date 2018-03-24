package cqrs

import cqrs.handler.AccountCommandHandler
import cqrs.model.{BankAccountId, CreateAccountCommand, DepositMoneyCommand, WithdrawMoneyCommand}
import cqrs.projection.AccountProjection


object Main {
  def main(args: Array[String]): Unit = {
    BankAccountEventBus.subscribe(AccountProjection)

    val result = for {
      create      <- AccountCommandHandler.handle(CreateAccountCommand(100))
      accountId   =  BankAccountId(create.aggregateId.value)
      _           <- AccountCommandHandler.handle(DepositMoneyCommand(accountId, 10))
      _           <- AccountCommandHandler.handle(WithdrawMoneyCommand(accountId, 10))
      _           <- AccountCommandHandler.handle(WithdrawMoneyCommand(accountId, 55))
      _           <- AccountCommandHandler.handle(WithdrawMoneyCommand(accountId, 30))
      accProj     <- Right(AccountProjection.get(accountId))
      withdrawT   <- Right(AccountProjection.getMoneyWithdrawingProjection(accountId))
    } yield (accProj, withdrawT)

    println(result)
  }
}
