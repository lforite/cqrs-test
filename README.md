# Project Title

A simple test to implement CQRS / ES mechanics around banking example.

## Getting Started

Clone the project and :

``` 
git clone https://github.com/lforite/cqrs-test.git
cd cqrs-test
sbt run
```

That's it !

## What it does

1) Commands are sent to a command handler
2) The validity of the command is checked against the aggregate
3) An event is stored in a DB and dispatched in an event bus (here simple in-memory queue) upon success
4) A projection is created out of those events
5) Projections are used for querying

## Playing around

You can change the Main class to play around with the project