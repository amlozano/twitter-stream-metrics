package es.amartin.twitterstreammetrics.toinflux.config

trait Configuration {

  def influxBatchSize: Int

  def influxDatabase: String

  def influxPassword: String

  def influxUser: String

  def influxHost: String

  def influxRetentionPolicy: String

  def kafkaBrokers: String

  def kafkaTopic: String

  def kafkaOffsetResetConfig: String

  def kafkaReadInterval: Long
}
