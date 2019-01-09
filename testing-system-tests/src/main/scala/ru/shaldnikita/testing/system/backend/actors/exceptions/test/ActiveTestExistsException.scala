package ru.shaldnikita.testing.system.backend.actors.exceptions.test

/**
  * @author Nikita Shaldenkov
  */
@SerialVersionUID(1L)
class ActiveTestExistsException(studentId: String)
  extends Exception(s"Student [$studentId] already has active test")
    with Serializable
