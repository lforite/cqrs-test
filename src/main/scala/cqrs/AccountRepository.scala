package cqrs



object AccountRepository {

  def getAggregate(bankAccountId: BankAccountId): Option[BankAccount] = {
    EventStore.retrieve(AggregateId(bankAccountId.value)).foldLeft[Option[BankAccount]](None) {
      case (_, c: AccountCreatedEvent) => Some(BankAccount(c.bankAccountId, c.amount))
      case (Some(acc), d: MoneyDepositedEvent) => Some(acc.copy(balance = acc.balance + d.amount))
      case (Some(acc), d: MoneyWithdrewEvent) => Some(acc.copy(balance = acc.balance - d.amount))
      case _ => None
    }
  }
}
