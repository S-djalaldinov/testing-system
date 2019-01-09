package ru.shaldnikita.testing.system.backend.actors.exceptions

/**
  * @author Nikita Shaldenkov
  */
@SerialVersionUID(1L)
class NoActiveTestException(studentId: String)
  extends Exception(s"No active test found for student [$studentId]")
    with Serializable
