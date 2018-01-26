package es.amartin.twitterstreammetrics.ingestion.config

trait Configuration {
  def twitterConsumerKey: String
  def twitterConsumerSecret: String
  def twitterToken: String
  def twitterTokenSecret: String
  def kafkaBrokers: String
  def kafkaTopic: String
  def zookeeperAddress: String
}