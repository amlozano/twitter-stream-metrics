package es.amartin.twitterstreammetrics.ingestion.config

import com.typesafe.config.Config

class Settings(config: Config) extends Configuration {

  override def twitterConsumerKey: String = config.getString("twitter.consumerKey")

  override def twitterConsumerSecret: String = config.getString("twitter.consumerSecret")

  override def twitterToken: String = config.getString("twitter.token")

  override def twitterTokenSecret: String = config.getString("twitter.tokenSecret")

  override def kafkaBrokers: String = config.getString("kafka.brokers")

  override def kafkaTopic: String = config.getString("kafka.topic")

  override def zookeeperAddress: String = config.getString("zookeeper.address")
}
