package es.amartin.twitterstreammetrics.kafka

trait KafkaConfiguration {
  def brokers: String

  def topic: String

  def offsetResetConfig: String

  def readInterval: Long
}
