package es.amartin.twitterstreammetrics.ingestion.source

import java.io.Closeable
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StreamingEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import com.typesafe.scalalogging.LazyLogging

class TwitterClient(oAuth1: OAuth1, endpoint: StreamingEndpoint) extends Closeable with LazyLogging {

  private val queueCapacity = 10000
  private val queue: BlockingQueue[String] = new LinkedBlockingQueue[String](queueCapacity)

  private val client: BasicClient = new ClientBuilder()
    .hosts(Constants.STREAM_HOST)
    .endpoint(endpoint)
    .authentication(oAuth1)
    .processor(new StringDelimitedProcessor(queue))
    .build()

  def connect(): Unit = {
    client.connect()
  }

  def nextTweet(): Option[String] = {
    if (client.isDone) {
      None
    } else {
      Some(queue.take())
    }
  }

  override def close(): Unit = {
    logger.info("Closing Tweeter Source...")
    client.stop()
  }
}
