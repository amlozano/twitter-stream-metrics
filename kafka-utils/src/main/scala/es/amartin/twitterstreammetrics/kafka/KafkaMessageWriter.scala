package es.amartin.twitterstreammetrics.kafka

import java.io.Closeable
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.Serde

trait MessageWriter extends Closeable {
  def write(message: String): Unit
}

class KafkaMessageWriter[K, V](generator: RecordGenerator[String, K, V],
                               keySerde: Serde[K],
                               valueSerde: Serde[V])(implicit config: KafkaConfiguration)
  extends MessageWriter with LazyLogging {
  private val properties = new Properties
  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.brokers)
  properties.put(ProducerConfig.ACKS_CONFIG, "all")

  private val kafkaProducer = new KafkaProducer[K, V](properties, keySerde.serializer(), valueSerde.serializer())

  override def write(message: String): Unit = {
    logger.info("sending message to kafka")
    kafkaProducer.send(generator.generateRecord(message))
  }

  override def close(): Unit = {
    logger.info("Closing Kafka Message Writer...")
    kafkaProducer.close()
  }
}
