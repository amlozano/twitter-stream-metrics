package es.amartin.twitterstreammetrics.toinflux.config

class ConfigurationStub(kafkaPort: Int = 9092, zookeeperPort: Int = 2181) extends Configuration {

  override def influxDatabase: String = "test"

  override def influxPassword: String = "password"

  override def influxUser: String = "user"

  override def influxHost: String = s"localhost:8086"

  override def kafkaBrokers: String = s"localhost:$kafkaPort"

  override def kafkaTopic: String = "test-topic"

  override def kafkaOffsetResetConfig: String = "earliest"

  override def influxRetentionPolicy: String = "autogen"

  override def influxBatchSize: Int = 100

  override def kafkaReadInterval: Long = 1000
}
