package es.amartin.twitterstreammetrics.ingestion.config

import es.amartin.twitterstreammetrics.kafka.KafkaConfiguration

trait Configuration {
  def twitterConsumerKey: String
  def twitterConsumerSecret: String
  def twitterToken: String
  def twitterTokenSecret: String
  def kafka: KafkaConfiguration
  def zookeeperAddress: String
}