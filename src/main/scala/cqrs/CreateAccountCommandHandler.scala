package cqrs

import java.util.UUID

object CreateAccountCommandHandler extends CommandHandler[CreateAccountCommand]{
  override def handle(command: CreateAccountCommand): Either[String, AccountCreatedEvent] = {
    for {
      _         <- validate(command)
      event     = AccountCreatedEvent(BankAccountId(UUID.randomUUID().toString), command.initialAmount)
      _         <- Right(EventStore.store(event))
    } yield event
  }


  private def validate(createAccountCommand: CreateAccountCommand): Either[String, Unit] = {
    if(createAccountCommand.initialAmount < 100)
      Left("Insufficient initial funds")
    else
      Right(())
  }

}
