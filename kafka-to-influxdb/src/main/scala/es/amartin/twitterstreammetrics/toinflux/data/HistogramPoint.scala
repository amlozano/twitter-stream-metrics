package es.amartin.twitterstreammetrics.toinflux.data

case class HistogramPoint(start: Long, end: Long, value: AggregationValue)