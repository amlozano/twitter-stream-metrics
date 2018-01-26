package es.amartin.twitterstreammetrics.toinflux.io

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, AggregationValue, HistogramPoint}

object MetricGenerator {

  def generateMetric(
    function: String,
    name: String,
    fields: Map[String, String],
    start: Long,
    end: Long,
    result: Number): (AggregationKey, HistogramPoint) = {
    val key = AggregationKey(function, name, fields)
    val value = HistogramPoint(start, end, AggregationValue(result, function))
    (key, value)
  }
}
