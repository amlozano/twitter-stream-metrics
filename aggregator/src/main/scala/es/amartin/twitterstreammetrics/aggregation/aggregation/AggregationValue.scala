package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.fasterxml.jackson.databind.JsonNode
import es.amartin.twitterstreammetrics.aggregation.aggregation.functions.AggregationFunction

case class AggregationValue(value: Number, aggregationFunction: String)

object AggregationValue {
  def apply(jsonNode: JsonNode, metricDefinition: MetricDefinition): AggregationValue = {
    val value = AggregationFunction(metricDefinition.aggregationFunction).applyFunction(jsonNode)
    val aggregationFunction = metricDefinition.aggregationFunction
    AggregationValue(value, aggregationFunction)
  }
}
