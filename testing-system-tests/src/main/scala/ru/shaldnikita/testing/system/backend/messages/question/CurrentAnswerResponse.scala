package ru.shaldnikita.testing.system.backend.messages.question

import ru.shaldnikita.testing.system.backend.entities.Answer

/**
  * @author Nikita Shaldenkov
  */
final case class CurrentAnswerResponse(answer: Option[Answer])
