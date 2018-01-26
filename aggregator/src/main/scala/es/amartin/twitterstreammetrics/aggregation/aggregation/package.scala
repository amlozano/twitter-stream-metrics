/**
  * Created by d065461 on 9/8/17.
  */
package es.amartin.twitterstreammetrics.aggregation

import com.fasterxml.jackson.databind.JsonNode

import scala.concurrent.duration.Duration

package object aggregation {
  type JsonPath = String
  type JsonDocument = Map[String, Any]
  type JsonFilter = JsonNode => Boolean
  type TimeAggregation = Duration
  type JsonTextFilter = Map[String, Set[String]]
}
