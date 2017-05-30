package cqrs

object WithdrawMoneyCommandHandler extends CommandHandler[WithdrawMoneyCommand] {
  def handle(withdrawMoneyCommand: WithdrawMoneyCommand): Either[String, Event] = {
    val aggregate = AccountRepository.getAggregate(withdrawMoneyCommand.id)

    validate(withdrawMoneyCommand, aggregate) match {
      case Left(r) => Left(r)
      case _ => val event = MoneyWithdrewEvent(withdrawMoneyCommand.id, withdrawMoneyCommand.amount)
        EventStore.store(event)
        Right(event)
    }
  }

  private def validate(withdrawMoneyCommand: WithdrawMoneyCommand, bankAccountOpt: Option[BankAccount]): Either[String, Unit] = {
    bankAccountOpt match {
      case None => Left("Bank account does not exist")
      case Some(bankAccount) if bankAccount.balance - withdrawMoneyCommand.amount < 0 => Left("Insufficient funds")
      case Some(bankAccount) if bankAccount.balance - withdrawMoneyCommand.amount > 0 => Right(())
    }
  }
}
