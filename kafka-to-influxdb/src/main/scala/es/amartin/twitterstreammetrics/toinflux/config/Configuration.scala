package es.amartin.twitterstreammetrics.toinflux.config

import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

trait Configuration {
  def influxdb: InfluxdbConfiguration

  def kafka: KafkaConfiguration
}

trait InfluxdbConfiguration {

  def batchSize: Int

  def database: String

  def password: String

  def user: String

  def host: String

  def retentionPolicy: String
}
