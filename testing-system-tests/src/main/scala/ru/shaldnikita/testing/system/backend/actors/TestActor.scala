package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{
  Actor,
  ActorLogging,
  ActorRef,
  OneForOneStrategy,
  PoisonPill,
  Props,
  SupervisorStrategy,
  Terminated
}
import ru.shaldnikita.testing.system.backend.actors.exceptions.test.{
  ActiveTestExistsException,
  NoActiveTestException
}
import ru.shaldnikita.testing.system.backend.entities.Test
import ru.shaldnikita.testing.system.backend.messages.test.{
  FinishTest,
  SaveTest,
  StartTest
}
import akka.pattern.ask
import akka.util.Timeout
import ru.shaldnikita.testing.system.backend.messages.{
  RandomQuestionsRequest,
  RandomQuestionsResponse
}

import scala.concurrent.ExecutionContext

object TestActor {
  def props(databaseUrls: Vector[String], questionsStorage: ActorRef)(
      implicit timeout: Timeout,
      ec: ExecutionContext) =
    Props(new TestActor(databaseUrls, questionsStorage))

  def name = "test-actor"
}

class TestActor(databaseUrls: Vector[String], questionsStorage: ActorRef)(
    implicit timeout: Timeout,
    ec: ExecutionContext)
    extends Actor
    with ActorLogging {

  require(databaseUrls.nonEmpty)

  val initialDatabaseUrl: String = databaseUrls.head
  var alternateDatabases: Vector[String] = databaseUrls.tail

  var databaseWriter: ActorRef = context.actorOf(
    DatabaseWriterActor.props(initialDatabaseUrl),
    DatabaseWriterActor.name(initialDatabaseUrl)
  )
  context.watch(databaseWriter)

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy()(SupervisorStrategy.defaultDecider)

  override def receive: Receive = onMessage(None)

  private def onMessage(currentTest: Option[Test])(
      implicit ec: ExecutionContext): Receive = {

    case Terminated(_) =>
      if (alternateDatabases.nonEmpty) {
        val newDatabaseUrl = alternateDatabases.head
        alternateDatabases = alternateDatabases.tail
        databaseWriter = context.actorOf(
          DatabaseWriterActor.props(newDatabaseUrl),
          DatabaseWriterActor.name(newDatabaseUrl)
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
      val activeTest =
        currentTest.getOrElse(throw new NoActiveTestException(studentId))
      databaseWriter ! SaveTest(activeTest)
      finishTest()
  }

  private def createTest(studentId: String)(
      implicit ec: ExecutionContext): Unit = {
    (questionsStorage ? RandomQuestionsRequest(10))
      .mapTo[RandomQuestionsResponse]
      .map(_.questions)
      .map(questions => context.become(onMessage(Some(Test(questions)))))
  }

  private def finishTest(): Unit = {
    context.become(onMessage(None))
  }
}
