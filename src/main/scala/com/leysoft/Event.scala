package com.leysoft

import java.util

import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer, StringSerializer}

case class Event(message: String)

case class EventSerializer[A <: Event]() extends Serializer[A] {

  val serializer = new StringSerializer

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit =
    serializer.configure(configs, isKey)

  override def serialize(topic: String, data: A): Array[Byte] =
    serializer.serialize(topic, Json.write(data))

  override def close(): Unit = serializer.close()
}

case class EventDeserializer[A <: Event]() extends Deserializer[A] {

  private val deserializer = new StringDeserializer

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit =
    deserializer.configure(configs, isKey)

  override def deserialize(topic: String, data: Array[Byte]): A =
    Json.read(deserializer.deserialize(topic, data))

  override def close(): Unit = deserializer.close()
}
