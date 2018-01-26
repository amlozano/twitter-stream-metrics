package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.fasterxml.jackson.databind.JsonNode

case class MetricDefinition(
  filter: JsonFilter,
  aggregationFunction: String,
  fieldExtractor: JsonNode => Map[String, String],
  defaultValue: Number,
  name: String
)