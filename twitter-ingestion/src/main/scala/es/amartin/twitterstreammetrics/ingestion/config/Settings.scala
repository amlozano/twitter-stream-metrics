package es.amartin.twitterstreammetrics.ingestion.config

import com.typesafe.config.Config
import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

class Settings(config: Config) extends Configuration {

  override def twitterConsumerKey: String = config.getString("twitter.consumerKey")

  override def twitterConsumerSecret: String = config.getString("twitter.consumerSecret")

  override def twitterToken: String = config.getString("twitter.token")

  override def twitterTokenSecret: String = config.getString("twitter.tokenSecret")

  override def kafka: KafkaConfiguration = new KafkaConfiguration {
    override def brokers: String = config.getString("kafka.brokers")

    override def topic: String = config.getString("kafka.topic")

    override def offsetResetConfig: String = "earliest"

    override def readInterval: Long = 10000
  }

  override def zookeeperAddress: String = config.getString("zookeeper.address")
}
