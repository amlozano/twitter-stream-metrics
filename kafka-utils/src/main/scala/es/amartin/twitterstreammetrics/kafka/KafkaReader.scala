package es.amartin.twitterstreammetrics.kafka

import java.time.Duration
import java.util.{Properties, UUID}

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.Serde

import scala.collection.JavaConverters._

class KafkaReader[K, V](keySerde: Serde[K], valueSerde: Serde[V])(implicit val settings: KafkaConfiguration) {
  private val properties = new Properties
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.offsetResetConfig)
  properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.brokers)
  properties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
  private val kafkaConsumer = new KafkaConsumer[K, V](properties, keySerde.deserializer(), valueSerde.deserializer())
  kafkaConsumer.subscribe(List(settings.topic).asJava)

  def readBatch(interval: Long): Iterable[(K, V)] = {
    kafkaConsumer.poll(Duration.ofMillis(interval)).records(settings.topic).asScala.map(
      record => {
        val key = record.key()
        val value = record.value()
        (key, value)
      }
    )
  }

  def close(): Unit ={
    kafkaConsumer.close()
  }

}