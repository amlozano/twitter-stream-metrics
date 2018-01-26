package es.amartin.twitterstreammetrics.toinflux.config

import com.typesafe.config.Config

class Settings(config: Config) extends Configuration {

  override def kafkaBrokers: String = config.getString("kafka.brokers")

  override def kafkaTopic: String = config.getString("kafka.topic")

  override def kafkaOffsetResetConfig: String = config.getString("kafka.offsetResetConfig")

  override def kafkaReadInterval: Long = config.getLong("kafka.readInterval")

  override def influxDatabase: String = config.getString("influx.database")

  override def influxPassword: String = config.getString("influx.password")

  override def influxUser: String = config.getString("influx.user")

  override def influxHost: String = config.getString("influx.host")

  override def influxRetentionPolicy: String = config.getString("influx.retentionPolicy")

  override def influxBatchSize: Int = config.getInt("influx.batchSize")
}
