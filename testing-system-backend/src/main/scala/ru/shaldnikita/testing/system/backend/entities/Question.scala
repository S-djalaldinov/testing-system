package ru.shaldnikita.testing.system.backend.entities

case class Question(questionText: String, correctAnswers: List[Answer], availableAnswers: List[Answer])

case class Answer(text: String)
