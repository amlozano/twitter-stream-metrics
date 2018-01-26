package es.amartin.twitterstreammetrics.toinflux

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.toinflux.config.{Configuration, Settings}
import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.influx.{InfluxDBClientImpl, InfluxKeyValWriterImpl, KeyValToPointConverterImpl}
import es.amartin.twitterstreammetrics.toinflux.io.{InfluxBatchWriter, KafkaReader}
import es.amartin.twitterstreammetrics.toinflux.serdes.JSONSerde

import scala.sys.addShutdownHook

object KafkaToInfluxApp extends App with LazyLogging {

  addShutdownHook {
    kafkaReader.close()
    influxWriter.close()
  }

  implicit val settings: Configuration = new Settings(ConfigFactory.load())

  val kafkaReader = new KafkaReader[AggregationKey, HistogramPoint](new JSONSerde[AggregationKey],
    new JSONSerde[HistogramPoint])

  val influxWriter = new InfluxBatchWriter(
    new InfluxKeyValWriterImpl(
      new InfluxDBClientImpl(settings
        .influxHost,
        settings.influxUser,
        settings.influxPassword),
      new KeyValToPointConverterImpl),
    settings.influxDatabase,
    settings.influxRetentionPolicy,
    settings.influxBatchSize)

  while (true) {
    influxWriter.writeItems(kafkaReader.readBatch(settings.kafkaReadInterval))
  }
}
