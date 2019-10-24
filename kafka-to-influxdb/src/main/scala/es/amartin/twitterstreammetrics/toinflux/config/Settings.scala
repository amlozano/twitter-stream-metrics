package es.amartin.twitterstreammetrics.toinflux.config

import com.typesafe.config.Config
import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

class Settings(config: Config) extends Configuration {
  override def kafka: KafkaConfiguration = new KafkaConfiguration {
    override def brokers: String = config.getString("kafka.brokers")

    override def topic: String = config.getString("kafka.topic")

    override def offsetResetConfig: String = config.getString("kafka.offsetResetConfig")

    override def readInterval: Long = config.getLong("kafka.readInterval")
  }

  override def influxdb: InfluxdbConfiguration = new InfluxdbConfiguration {
    override def batchSize: Int = config.getInt("influx.batchSize")

    override def database: String = config.getString("influx.database")

    override def password: String = config.getString("influx.password")

    override def user: String = config.getString("influx.user")

    override def host: String = config.getString("influx.host")

    override def retentionPolicy: String = config.getString("influx.retentionPolicy")
  }
}
