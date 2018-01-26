package es.amartin.twitterstreammetrics.toinflux.data

case class AggregationKey(aggregationFunction: String, aggregationName: String, fields: Map[String, String])
