package es.amartin.twitterstreammetrics.aggregation.aggregation

case class AggregationKey(aggregationFunction: String, aggregationName: String, fields: Map[String, String])
