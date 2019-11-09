package com.leysoft

object Parameters {

  val bootstrapServers = "localhost:9092"
  val topic = "scala.topic"

  object ProducerParameters {
    val `clientId` = "scala.client"
    val `acks` = "all"
    val `retries` = "3"
    val `compression` = "snappy"
    val `idempotence` = "true"
    val `batchSize` = "500"
    val `lingerMilliseconds` = "10"
  }

  object ConsumerParameters {
    val `groupId` = "scala.group"
    val `autoCommit` = "false"
    val `autoOffset` = "earliest"
    val `insolation` = "read_committed"
  }
}
