package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ru.shaldnikita.testing.system.backend.entities.{Answer, Question}
import ru.shaldnikita.testing.system.backend.messages.RandomQuestionsRequest
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.io.Source._
import scala.util.Random

/**
  * @author Nikita Shaldenkov
  */
class QuestionsStorageActor
    extends Actor
    with ActorLogging
    with QuestionFormatSupport {

  private val questionsJson = fromInputStream(
    this.getClass.getClassLoader.getResourceAsStream("questions.txt"),
    "UTF-8").mkString
  private val questions = questionsJson.parseJson.convertTo[List[Question]]

  override def receive: Receive = {
    case RandomQuestionsRequest(quantity) =>
      sender() ! generateRandomQuestions(quantity)
  }

  private def generateRandomQuestions(quantity: Int): Unit = {
    var numbers: Set[Int] = Set.empty
    while (numbers.size < quantity) {
      numbers = numbers + Random.nextInt(questions.size)
    }
    numbers.map(i => questions(i)).toSeq
  }
}

trait QuestionFormatSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val answerFormat: RootJsonFormat[Answer] = jsonFormat1(Answer)
  implicit val questionFormat: RootJsonFormat[Question] = jsonFormat3(Question)
}
