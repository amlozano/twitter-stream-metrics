package es.amartin.twitterstreammetrics.aggregation.io

import org.apache.kafka.streams.kstream.KStream

trait StreamWriter[K,V] {
  def write(stream: KStream[K,V]): Unit
}
