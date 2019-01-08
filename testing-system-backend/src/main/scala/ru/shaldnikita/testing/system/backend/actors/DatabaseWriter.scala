package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}
import ru.shaldnikita.testing.system.backend.messages.test.StartTest

object DatabaseWriter {
  def props(databaseUrl: String) = Props(new DatabaseWriter(databaseUrl))

  def name(databaseUrl: String) =
    s"""db-writer-${databaseUrl.split("/").last}"""

  // A line in the log file parsed by the LogProcessor Actor
  case class Line(time: Long, message: String, messageType: String)

}

class DatabaseWriter(databaseUrl: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StartTest(studentId) => startTest(studentId)
  }


  private def startTest(studentId: String): Unit = {
    log.info("Started test for student {}", studentId)
  }
}
