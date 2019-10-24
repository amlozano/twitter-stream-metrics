package es.amartin.twitterstreammetrics.toinflux.config

import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

class ConfigurationStub(kafkaPort: Int = 9092, zookeeperPort: Int = 2181) extends Configuration {

  override def influxdb: InfluxdbConfiguration = new InfluxdbConfiguration {
    override def batchSize: Int = 100

    override def database: String = "test"

    override def password: String = "password"

    override def user: String = "user"

    override def host: String = "localhost:8086"

    override def retentionPolicy: String = "autogen"
  }

  override def kafka: KafkaConfiguration = new KafkaConfiguration {
    override def brokers: String = s"localhost:$kafkaPort"

    override def topic: String = "test-topic"

    override def offsetResetConfig: String = "earliest"

    override def readInterval: Long = 1000
  }
}
