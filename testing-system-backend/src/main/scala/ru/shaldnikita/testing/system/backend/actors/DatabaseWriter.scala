package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}
import ru.shaldnikita.testing.system.backend.messages.test.{
  FinishTest,
  StartTest
}

object DatabaseWriter {
  def props(databaseUrl: String) = Props(new DatabaseWriter(databaseUrl))

  def name(databaseUrl: String) =
    s"""db-writer-${databaseUrl.split("/").last}"""
}

class DatabaseWriter(databaseUrl: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StartTest(studentId)  => startTest(studentId)
    case FinishTest(studentId) => finishTest(studentId)
  }

  private def finishTest(studentId: String): Unit = {
    //write to db
    log.info("Finished test for student")
  }

  private def startTest(studentId: String): Unit = {
    //write to db
    log.info("Started test for student {}", studentId)
  }
}
