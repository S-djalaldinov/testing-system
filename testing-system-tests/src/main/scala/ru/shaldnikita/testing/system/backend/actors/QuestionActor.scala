package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}
import ru.shaldnikita.testing.system.backend.actors.exceptions.question.{
  AnswerAlreadySelectedException,
  InvalidAnswerException
}
import ru.shaldnikita.testing.system.backend.entities.{Answer, Question}
import ru.shaldnikita.testing.system.backend.messages.question.{
  CurrentAnswerRequest,
  CurrentAnswerResponse,
  SelectAnswer
}

object QuestionActor {
  def props(question: Question) = Props(new QuestionActor(question))

  def name(question: Question) = s"question-${question.id}"
}

class QuestionActor(question: Question) extends Actor with ActorLogging {

  override def receive: Receive = onMessage(None)

  private def onMessage(selectedAnswer: Option[Answer]): Receive = {

    case SelectAnswer(answer) =>
      selectedAnswer.map(a => throw new AnswerAlreadySelectedException(a))
      selectAnswer(answer)

    case CurrentAnswerRequest =>
      sender() ! CurrentAnswerResponse(selectedAnswer)
  }

  def selectAnswer(answer: Answer): Unit = {
    if (question.availableAnswers.contains(answer)) {
      context.become(onMessage(Some(answer)))
      log.debug(s"Selected $answer")
    } else throw new InvalidAnswerException(answer)
  }

}
