package cqrs.model

sealed trait Command {
  type AggregateType <: Aggregate
}

sealed trait AccountCommand extends Command {
  override type AggregateType = BankAccount
}
case class CreateAccountCommand(initialAmount: Int) extends AccountCommand
case class WithdrawMoneyCommand(id: BankAccountId, amount: Int) extends AccountCommand
case class DepositMoneyCommand(id: BankAccountId, amount: Int) extends AccountCommand
