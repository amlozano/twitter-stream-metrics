package es.amartin.twitterstreammetrics.aggregation.config

trait Configuration {
  def kafkaThreads: Integer
  def kafkaBrokers: String
  def kafkaInputTopic: String
  def kafkaOutputTopic: String
  def kafkaOffsetResetConfig: String
  def kafkaApplicationId: String
  def kafkaStateDir: String
  def metricDefinitionDirectory: String
}
