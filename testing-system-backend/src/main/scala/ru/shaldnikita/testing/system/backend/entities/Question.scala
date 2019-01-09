package ru.shaldnikita.testing.system.backend.entities

final case class Question(questionText: String,
                          correctAnswers: List[Answer],
                          availableAnswers: List[Answer])

final case class Answer(text: String)
