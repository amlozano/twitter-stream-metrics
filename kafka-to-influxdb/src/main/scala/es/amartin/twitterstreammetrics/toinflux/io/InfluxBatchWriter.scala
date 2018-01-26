package es.amartin.twitterstreammetrics.toinflux.io

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.influx.InfluxKeyValWriter

class InfluxBatchWriter(
  influxKeyValWriter: InfluxKeyValWriter,
  database: String,
  retentionPolicy: String,
  batchSize: Int) {
  private var closed = false

  def writeItems(items: Iterable[(AggregationKey, HistogramPoint)]): Unit = {
    val itemsIterator = items.iterator
    while(itemsIterator.nonEmpty) {
      val batch = itemsIterator.take(batchSize)
      influxKeyValWriter.writeItems(batch.toList, database, retentionPolicy)
    }
  }

  def close(): Unit = {
    closed = true
  }
}
