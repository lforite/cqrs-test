package cqrs



trait CommandHandler[T <: Command] {
  def handle(command: T): Either[String, Event]
}

object CommandHandler extends CommandHandler[Command] {
  def handle(command: Command): Either[String, Event] = {
    val result = command match {
      case d: DepositMoneyCommand => DepositMoneyCommandHandler.handle(d)
      case c: WithdrawMoneyCommand => WithdrawMoneyCommandHandler.handle(c)
      case c: CreateAccountCommand => CreateAccountCommandHandler.handle(c)
    }

    for {
      r         <- result
      published <- EventBus.publish(r)
    } yield r
  }
}