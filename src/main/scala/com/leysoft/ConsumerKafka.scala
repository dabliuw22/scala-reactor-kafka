package com.leysoft

import java.util.Properties

import akka.actor.ActorSystem
import com.leysoft.Parameters._
import com.leysoft.Parameters.ConsumerParameters._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import reactor.kafka.receiver.{KafkaReceiver, ReceiverOptions, ReceiverRecord}

import scala.jdk.CollectionConverters._

object ConsumerKafka extends App {
  val system = ActorSystem("consumer-system")

  val consumerSettings = new Properties()
  consumerSettings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  consumerSettings.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    classOf[StringDeserializer])
  consumerSettings.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    classOf[EventDeserializer[_]])
  consumerSettings.put(ConsumerConfig.GROUP_ID_CONFIG, `groupId`)
  consumerSettings.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, `autoCommit`)
  consumerSettings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, `autoOffset`)
  consumerSettings.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, `insolation`)

  val receiverOptions: ReceiverOptions[String, Event] = ReceiverOptions.create(consumerSettings)
    .subscription(List(topic).asJava)
  val receiver: KafkaReceiver[String, Event] = KafkaReceiver.create(receiverOptions)

  val process = (message: ReceiverRecord[String, Event]) => {
    system.log.info(s"Consumer event: ${message.value}")
    message
  }

  val commit = (message: ReceiverRecord[String, Event]) => {
    system.log.info(s"Commit $message...")
    message.receiverOffset.commit().thenReturn(message.value())
  }

  receiver.receive()
    .map { process(_) }
    .map { commit(_) }
    .subscribe()
}
