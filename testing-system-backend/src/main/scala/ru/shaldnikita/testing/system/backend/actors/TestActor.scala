package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy, Terminated}
import ru.shaldnikita.testing.system.backend.actors.exceptions.NoActiveTestException
import ru.shaldnikita.testing.system.backend.actors.exceptions.test.{ActiveTestExistsException, NoActiveTestException}
import ru.shaldnikita.testing.system.backend.entities.Test
import ru.shaldnikita.testing.system.backend.messages.test.{FinishTest, SaveTest, StartTest}

object TestActor {
  def props(databaseUrls: Vector[String]) =
    Props(new TestActor(databaseUrls))

  def name = "file-watcher-supervisor"
}

class TestActor(databaseUrls: Vector[String]) extends Actor with ActorLogging {

  require(databaseUrls.nonEmpty)

  val initialDatabaseUrl: String = databaseUrls.head
  var alternateDatabases: Vector[String] = databaseUrls.tail

  var databaseWriter: ActorRef = context.actorOf(
    DatabaseWriter.props(initialDatabaseUrl),
    DatabaseWriter.name(initialDatabaseUrl)
  )
  context.watch(databaseWriter)

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy()(SupervisorStrategy.defaultDecider)

  override def receive: Receive = onMessage(None)

  private def onMessage(currentTest: Option[Test]): Receive = {

    case Terminated(_) =>
      if (alternateDatabases.nonEmpty) {
        val newDatabaseUrl = alternateDatabases.head
        alternateDatabases = alternateDatabases.tail
        databaseWriter = context.actorOf(
          DatabaseWriter.props(newDatabaseUrl),
          DatabaseWriter.name(newDatabaseUrl)
        )
        context.watch(databaseWriter)
      } else {
        log.error("All Db nodes broken, stopping.")
        self ! PoisonPill
      }

    case StartTest(studentId) =>
      currentTest.map(throw new ActiveTestExistsException(studentId))
      createTest(studentId)

    case FinishTest(studentId) =>
      val activeTest = currentTest.getOrElse(throw new NoActiveTestException(studentId))
      databaseWriter ! SaveTest(activeTest)
      finishTest()
  }

  private def createTest(studentId: String): Unit = {
    context.become(onMessage(Some(Test())))
  }

  private def finishTest(): Unit = {
    context.become(onMessage(None))
  }
}
