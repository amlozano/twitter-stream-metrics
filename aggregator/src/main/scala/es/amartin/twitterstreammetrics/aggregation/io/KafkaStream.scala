package es.amartin.twitterstreammetrics.aggregation.io

import java.util.Properties

import es.amartin.twitterstreammetrics.aggregation.config.Configuration
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

class KafkaStream(implicit val settings: Configuration) {
  private val properties = new Properties
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.kafkaOffsetResetConfig)
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, settings.kafkaApplicationId)
  properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, settings.kafkaBrokers)
  properties.put(StreamsConfig.STATE_DIR_CONFIG, settings.kafkaStateDir)
  properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, settings.kafkaThreads)
  properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass)
  properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)

  private val builder = new StreamsBuilder()

  private val streaming: KStream[Integer, String] = builder
    .stream(settings.kafkaInputTopic)

  def stream: KafkaStreams = new KafkaStreams(builder.build(), properties)

  def inputStream: KStream[Integer, String] = streaming
}
