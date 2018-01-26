package es.amartin.twitterstreammetrics.toinflux.io

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.influx.InfluxKeyValWriter
import org.scalatest.Matchers

class InfluxKeyValWriterStub(expectedDatabase: String, expectedRetentionPolicy: String)
  extends InfluxKeyValWriter with Matchers {
  private var elements = Map[AggregationKey, HistogramPoint]()
  private var calls = 0

  override def writeItems(
    values: Iterable[(AggregationKey, HistogramPoint)],
    database: String,
    retentionPolicy: String): Unit = {

    database shouldBe expectedDatabase
    retentionPolicy shouldBe expectedRetentionPolicy

    values.foreach(tuple => {
      val (key, value) = tuple
      elements += (key -> value)
    })
    calls += 1
  }

  def writtenElements: Map[AggregationKey, HistogramPoint] = elements

  def madeCalls: Int = calls
}
