package es.amartin.twitterstreammetrics.aggregation.config

import java.util.UUID

class ConfigurationStub(kafkaPort: Int = 9092, zookeeperPort: Int = 2181) extends Configuration {

  override def kafkaInputTopic: String = "test-input-topic"

  override def kafkaOutputTopic: String = "test-output-topic"

  override def kafkaOffsetResetConfig: String = "earliest"

  override def kafkaStateDir: String = "/tmp"

  override def kafkaBrokers: String = s"localhost:$kafkaPort"

  override def kafkaApplicationId: String = UUID.randomUUID().toString

  override def metricDefinitionDirectory: String = "/tmp/metrics"

  override def kafkaThreads: Integer = 1
}
