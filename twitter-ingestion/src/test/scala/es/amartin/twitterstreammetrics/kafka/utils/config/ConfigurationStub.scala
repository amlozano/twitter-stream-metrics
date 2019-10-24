package es.amartin.twitterstreammetrics.kafka.utils.config

import es.amartin.twitterstreammetrics.ingestion.config.Configuration
import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

class ConfigurationStub(kafkaPort: Int = 9092, zookeeperPort: Int = 2181) extends Configuration {
  override def twitterConsumerKey: String = ""

  override def twitterConsumerSecret: String = ""

  override def twitterToken: String = ""

  override def twitterTokenSecret: String = ""

  override def kafka: KafkaConfiguration = new KafkaConfiguration {
    override def brokers: String = s"localhost:$kafkaPort"

    override def topic: String = "test-topic"

    override def offsetResetConfig: String = ""

    override def readInterval: Long = 10000
  }

  override def zookeeperAddress: String = s"localhost:$zookeeperPort"
}
