package es.amartin.twitterstreammetrics.ingestion

import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint
import com.twitter.hbc.httpclient.auth.OAuth1
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.ingestion.config.{Configuration, Settings}
import es.amartin.twitterstreammetrics.ingestion.source.TwitterClient
import es.amartin.twitterstreammetrics.kafka.{KafkaConfiguration, KafkaMessageWriter, MessageToOnlyByteValueRecordGenerator}
import org.apache.kafka.common.serialization.Serdes.{ByteArraySerde, IntegerSerde}

import scala.sys.addShutdownHook

object TwitterIngestionApp extends App with LazyLogging {

  implicit val settings: Configuration = new Settings(ConfigFactory.load())
  implicit val kafkaSettings: KafkaConfiguration = settings.kafka

  addShutdownHook {
    input.close()
    output.close()
  }

  val sourceOAuth = new OAuth1(
    settings.twitterConsumerKey,
    settings.twitterConsumerSecret,
    settings.twitterToken,
    settings.twitterTokenSecret
  )

  val input = new TwitterClient(sourceOAuth, new StatusesSampleEndpoint)

  val topic = settings.kafka.topic
  val recordGenerator = new MessageToOnlyByteValueRecordGenerator(topic)
  val output = new KafkaMessageWriter(recordGenerator, new IntegerSerde, new ByteArraySerde)

  input.connect()

  while (true) {
    output.write(input.nextTweet().get)
  }

}
