package es.amartin.twitterstreammetrics.ingestion.dest.config

import es.amartin.twitterstreammetrics.ingestion.config.Configuration

class ConfigurationStub(kafkaPort: Int = 9092, zookeeperPort: Int = 2181) extends Configuration {
  override def twitterConsumerKey: String = ""

  override def twitterConsumerSecret: String = ""

  override def twitterToken: String = ""

  override def twitterTokenSecret: String = ""

  override def kafkaBrokers: String = s"localhost:$kafkaPort"

  override def kafkaTopic: String = "test-topic"

  override def zookeeperAddress: String = s"localhost:$zookeeperPort"
}
