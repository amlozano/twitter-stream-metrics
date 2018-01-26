package es.amartin.twitterstreammetrics.aggregation.config

import com.typesafe.config.Config

class Settings(config: Config) extends Configuration {

  override def kafkaBrokers: String = config.getString("kafka.brokers")

  override def kafkaInputTopic: String = config.getString("kafka.inputTopic")

  override def kafkaOutputTopic: String = config.getString("kafka.outputTopic")

  override def kafkaOffsetResetConfig: String = config.getString("kafka.offsetResetConfig")

  override def kafkaThreads: Integer = config.getInt("kafka.threads")

  override def kafkaStateDir: String = config.getString("kafka.stateDir")

  override def kafkaApplicationId: String = config.getString("kafka.applicationId")

  override def metricDefinitionDirectory: String = config.getString("metricDefinitionDir")
}
