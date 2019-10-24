package es.amartin.twitterstreammetrics.toinflux

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.kafka.{KafkaConfiguration, KafkaReader}
import es.amartin.twitterstreammetrics.toinflux.config.{Configuration, Settings}
import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.influx.{InfluxDBClientImpl, InfluxKeyValWriterImpl, KeyValToPointConverterImpl}
import es.amartin.twitterstreammetrics.toinflux.io.InfluxBatchWriter
import es.amartin.twitterstreammetrics.toinflux.serdes.JSONSerde

import scala.sys.addShutdownHook

object KafkaToInfluxApp extends App with LazyLogging {

  addShutdownHook {
    kafkaReader.close()
    influxWriter.close()
  }

  implicit val settings: Configuration = new Settings(ConfigFactory.load())
  implicit val kafkaSettings: KafkaConfiguration = settings.kafka

  val kafkaReader = new KafkaReader[AggregationKey, HistogramPoint](new JSONSerde[AggregationKey],
    new JSONSerde[HistogramPoint])

  val influxWriter = new InfluxBatchWriter(
    new InfluxKeyValWriterImpl(
      new InfluxDBClientImpl(settings
        .influxdb.host,
        settings.influxdb.user,
        settings.influxdb.password),
      new KeyValToPointConverterImpl),
    settings.influxdb.database,
    settings.influxdb.retentionPolicy,
    settings.influxdb.batchSize)

  while (true) {
    influxWriter.writeItems(kafkaReader.readBatch(settings.kafka.readInterval))
  }
}
