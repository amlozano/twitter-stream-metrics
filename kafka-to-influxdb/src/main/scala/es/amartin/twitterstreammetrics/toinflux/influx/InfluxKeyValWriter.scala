
package es.amartin.twitterstreammetrics.toinflux.influx

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}

trait InfluxKeyValWriter {
  def writeItems(values: Iterable[(AggregationKey, HistogramPoint)], database: String, retentionPolicy: String)
}

class InfluxKeyValWriterImpl(
  influxClient: InfluxDBClient,
  keyValToPointConverter: KeyValToPointConverter) extends InfluxKeyValWriter {

  override def writeItems(
    values: Iterable[(AggregationKey, HistogramPoint)],
    database: String,
    retentionPolicy: String): Unit = {
    val points = values.map(elem => {
      val (key, value) = elem
      keyValToPointConverter.convert(key, value)
    })
    influxClient.writeItems(database, retentionPolicy, points)
  }
}
