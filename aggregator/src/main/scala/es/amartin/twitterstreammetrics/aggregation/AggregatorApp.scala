package es.amartin.twitterstreammetrics.aggregation

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.aggregation.aggregation._
import es.amartin.twitterstreammetrics.aggregation.config.{Configuration, Settings}
import es.amartin.twitterstreammetrics.aggregation.io.{KafkaStream, TwitterMetricKafkaStreamWriter}

import scala.sys.addShutdownHook

object AggregatorApp extends App with LazyLogging {

  addShutdownHook {
    kafkaStream.stream.close()
  }

  implicit val settings: Configuration = new Settings(ConfigFactory.load())

  val kafkaStream = new KafkaStream()

  val metricDefinitionRetriever = new CachedMetricDefinitionRetriever(
    new FileMetricDefinitionRetriever(
      settings.metricDefinitionDirectory,
      new MetricDefinitionJsonParser,
      new LocalFileRetriever
    ))

  val streamWriter = new TwitterMetricKafkaStreamWriter(metricDefinitionRetriever, settings.kafkaOutputTopic)

  streamWriter.write(kafkaStream.inputStream)

  kafkaStream.stream.start()
}
