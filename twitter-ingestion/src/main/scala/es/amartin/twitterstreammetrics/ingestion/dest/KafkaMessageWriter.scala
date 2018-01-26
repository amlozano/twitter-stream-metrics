package es.amartin.twitterstreammetrics.ingestion.dest

import java.io.Closeable
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.ingestion.config.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}

trait MessageWriter extends Closeable {
  def write(message: String): Unit
}

class KafkaMessageWriter(generator: RecordGenerator[String, Int, Array[Byte]])(implicit config: Configuration)
  extends MessageWriter with LazyLogging {
  private val keySerializer = "org.apache.kafka.common.serialization.IntegerSerializer"
  private val valueSerializer = "org.apache.kafka.common.serialization.ByteArraySerializer"
  private val properties = new Properties
  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaBrokers)
  properties.put(ProducerConfig.ACKS_CONFIG, "all")
  properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
  properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)

  private val kafkaProducer = new KafkaProducer[Int, Array[Byte]](properties)



  override def write(message: String): Unit = {
    logger.info("sending message to kafka")
    kafkaProducer.send(generator.generateRecord(message))
  }

  override def close(): Unit = {
    logger.info("Closing Kafka Message Writer...")
    kafkaProducer.close()
  }
}
