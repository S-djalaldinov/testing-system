package ru.shaldnikita.testing.system.backend.actors

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{CallingThreadDispatcher, TestKit}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpecLike}
import ru.shaldnikita.testing.system.backend.actors.QuestionActorTests._
import ru.shaldnikita.testing.system.backend.entities.{Answer, Question}
import ru.shaldnikita.testing.system.backend.messages.question.{CurrentAnswerRequest, SelectAnswer}

class QuestionActorTests extends TestKit(testSystem)
  with WordSpecLike
  with StopSystemAfterAll
  with Matchers
  with ScalaFutures {

  private implicit val timeout: Timeout = Timeout(1, TimeUnit.SECONDS)
  private val testQuestion = Question("Is akka cool?", List(Answer("Yes!")), List(Answer("Yes!"), Answer("No!")))

  "QuestionActor" must {
    "set current answer when a SelectAnswer(Answer(\"Answer1\")) is sent to it and hes`s available one" in {

      val dispatcherId = CallingThreadDispatcher.Id
      val props = QuestionActor.props(testQuestion).withDispatcher(dispatcherId)
      val questionActor = system.actorOf(props)
      val selectedAnswer = testQuestion.availableAnswers.head

      questionActor ! SelectAnswer(selectedAnswer)
      val current = (questionActor ? CurrentAnswerRequest).mapTo[Option[Answer]].futureValue
      current shouldEqual Some(selectedAnswer)
    }
  }

}

object QuestionActorTests {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
         akka.loggers = [akka.testkit.TestEventListener]
      """)
    ActorSystem("testsystem", config)
  }
}