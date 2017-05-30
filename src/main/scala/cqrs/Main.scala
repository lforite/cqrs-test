package cqrs



object Main {
  def main(args: Array[String]): Unit = {
    EventBus.subscribe(AccountProjection)

    val result = for {
      create      <- CommandHandler.handle(CreateAccountCommand(100))
      accountId   =  BankAccountId(create.aggregateId.value)
      deposit     <- CommandHandler.handle(DepositMoneyCommand(accountId, 10))
      withdraw    <- CommandHandler.handle(WithdrawMoneyCommand(accountId, 10))
      withdraw    <- CommandHandler.handle(WithdrawMoneyCommand(accountId, 55))
      withdraw    <- CommandHandler.handle(WithdrawMoneyCommand(accountId, 30))
      accProj     <- Right(AccountProjection.get(accountId))
      withdrawT   <- Right(AccountProjection.getMoneyWithdrawingProject(accountId))
    } yield (accProj, withdrawT)

    println(result)
  }
}
