package ru.shaldnikita.testing.system.backend.actors.exceptions

import ru.shaldnikita.testing.system.backend.entities.Answer

@SerialVersionUID(1L)
class InvalidAnswerException(answer: Answer) extends Exception(s"Received not allowed answer [$answer]")
  with Serializable