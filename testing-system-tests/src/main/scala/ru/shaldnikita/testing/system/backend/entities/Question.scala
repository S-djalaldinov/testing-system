package ru.shaldnikita.testing.system.backend.entities

import java.util.UUID

final case class Question(questionText: String,
                          correctAnswers: List[Answer],
                          availableAnswers: List[Answer]) {

  val id: String = UUID.randomUUID().toString
}

final case class Answer(text: String)
