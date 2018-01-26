package es.amartin.twitterstreammetrics.aggregation.aggregation.functions

import com.fasterxml.jackson.databind.JsonNode

abstract class AggregationFunction {
  def applyFunction(node: JsonNode): Number

  def function(elem1: Number, elem2: Number): Number
}

object AggregationFunction {
  def apply(aggregationFunction: String): AggregationFunction = {
    aggregationFunction match {
      case "count" => new CountFunction
      case _ => throw new IllegalArgumentException(s"AggregationFunction $aggregationFunction not defined")
    }
  }
}