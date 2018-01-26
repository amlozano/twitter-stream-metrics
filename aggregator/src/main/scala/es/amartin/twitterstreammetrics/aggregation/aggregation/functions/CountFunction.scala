package es.amartin.twitterstreammetrics.aggregation.aggregation.functions

import com.fasterxml.jackson.databind.JsonNode

class CountFunction extends AggregationFunction {
  override def applyFunction(node: JsonNode): Number = 1

  override def function(elem1: Number, elem2: Number): Number = elem1.longValue() + elem2.longValue()
}
