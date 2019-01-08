package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}
import ru.shaldnikita.testing.system.backend.actors.exceptions.InvalidAnswerException
import ru.shaldnikita.testing.system.backend.entities.{Answer, Question}
import ru.shaldnikita.testing.system.backend.messages.question.{GetCurrentAnswer, SelectAnswer}

object QuestionActor {
  def props(question: Question) = Props(new QuestionActor(question))

  def name(question: Question) = s"question-${question.questionText}"
}

class QuestionActor(question: Question) extends Actor with ActorLogging {
  var selectedAnswer: Option[Answer] = None

  override def receive: Receive = {
    case SelectAnswer(answer) =>
      if (question.availableAnswers.contains(answer)) {
        this.selectedAnswer = Some(answer)
        log.debug(s"Selected $answer")
      }
      else throw new InvalidAnswerException(answer)

    //TODO replace with message type instead of entity
    case GetCurrentAnswer => sender() ! selectedAnswer
  }
}
