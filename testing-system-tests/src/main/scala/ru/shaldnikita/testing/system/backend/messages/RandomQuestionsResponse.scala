package ru.shaldnikita.testing.system.backend.messages

import ru.shaldnikita.testing.system.backend.entities.Question

/**
  * @author Nikita Shaldenkov
  */

final case class RandomQuestionsResponse(questions: List[Question])
