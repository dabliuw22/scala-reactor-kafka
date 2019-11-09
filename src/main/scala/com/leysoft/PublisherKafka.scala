package com.leysoft

import java.util.Properties

import akka.actor.ActorSystem
import com.leysoft.Parameters._
import com.leysoft.Parameters.ProducerParameters._
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import reactor.core.publisher.Flux
import reactor.kafka.sender.{KafkaSender, SenderOptions, SenderRecord}

object PublisherKafka extends App {
  implicit val system = ActorSystem("publisher-system")

  val producerSettings = new Properties
  producerSettings.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  producerSettings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    classOf[StringSerializer])
  producerSettings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    classOf[EventSerializer[Event]])
  producerSettings.put(ProducerConfig.CLIENT_ID_CONFIG, `clientId`)
  producerSettings.put(ProducerConfig.BATCH_SIZE_CONFIG, `batchSize`)
  producerSettings.put(ProducerConfig.LINGER_MS_CONFIG, `lingerMilliseconds`)
  producerSettings.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, `compression`)
  producerSettings.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, `idempotence`)
  producerSettings.put(ProducerConfig.RETRIES_CONFIG, `retries`)
  producerSettings.put(ProducerConfig.ACKS_CONFIG, `acks`)

  val senderOptions: SenderOptions[String, Event] = SenderOptions.create(producerSettings)
  val sender: KafkaSender[String, Event] = KafkaSender.create(senderOptions)

  val records = Flux.just("Reactor", "Scala", "Kafka", "Streams")
    .map { Event(_) }
    .map { event => SenderRecord.create(new ProducerRecord[String, Event](topic, event), event) }

  sender.send(records).subscribe()
  system.registerOnTermination { sender.close() }
}
