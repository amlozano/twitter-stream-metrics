package es.amartin.twitterstreammetrics.toinflux.io

import java.util.{Properties, UUID}

import es.amartin.twitterstreammetrics.toinflux.config.Configuration
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import collection.JavaConverters._



class KafkaReader[K, V](keySerde: Serde[K], valueSerde: Serde[V])(implicit val settings: Configuration) {
  private val properties = new Properties
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.kafkaOffsetResetConfig)
  properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.kafkaBrokers)
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.kafkaOffsetResetConfig)
  properties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
  private val kafkaConsumer = new KafkaConsumer[K, V](properties, keySerde.deserializer(), valueSerde.deserializer())
  kafkaConsumer.subscribe(List(settings.kafkaTopic).asJava)

  def readBatch(interval: Long): Iterable[(K, V)] = {
    kafkaConsumer.poll(interval).records(settings.kafkaTopic).asScala.map(
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