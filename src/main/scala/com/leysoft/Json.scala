package com.leysoft

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.util.{Success, Try}

object Json {

  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def write[A <: Event](data: A) = Try(mapper.writeValueAsString(data)) match {
    case Success(value) => value
    case _ => throw new RuntimeException("Write Error")
  }

  def read[A <: Event](data: String) = Try(mapper.readValue(data, classOf[Event]).asInstanceOf[A]) match {
    case Success(value) => value
    case _ => throw new RuntimeException("Read Error")
  }
}
