package ru.shaldnikita.testing.system.backend.actors.exceptions.question

import ru.shaldnikita.testing.system.backend.entities.Answer

/**
  * @author Nikita Shaldenkov
  */
@SerialVersionUID(1L)
class AnswerAlreadySelectedException(answer: Answer)
    extends Exception(s"Answer already selected: [$answer]")
    with Serializable
