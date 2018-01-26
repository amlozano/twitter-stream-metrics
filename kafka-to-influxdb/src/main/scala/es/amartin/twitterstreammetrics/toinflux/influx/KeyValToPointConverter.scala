package es.amartin.twitterstreammetrics.toinflux.influx

import java.util.concurrent.TimeUnit

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import org.influxdb.dto.Point
import collection.JavaConverters._

trait KeyValToPointConverter {
  def convert(key: AggregationKey, value: HistogramPoint): Point
}

class KeyValToPointConverterImpl extends KeyValToPointConverter {
  override def convert(
    key: AggregationKey,
    value: HistogramPoint): Point = {
    val measurement = key.aggregationName
    val tags = key.fields.asJava
    val time = value.start
    val precision = TimeUnit.MILLISECONDS
    val fieldName = value.value.aggregationFunction
    val fieldValue = value.value.value
    Point.measurement(measurement).tag(tags).time(time, precision).addField(fieldName, fieldValue).build()
  }
}
