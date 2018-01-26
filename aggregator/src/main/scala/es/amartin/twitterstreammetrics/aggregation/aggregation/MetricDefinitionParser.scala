package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import scala.collection.JavaConversions._

trait MetricDefinitionParser {
  def parse(metricDefinition: String): MetricDefinition
}

class MetricDefinitionJsonParser extends MetricDefinitionParser {
  override def parse(metricDefinition: String): MetricDefinition = {
    val jsonMetricDefinition = JsonUtils.to(metricDefinition, classOf[JsonMetricDefinition])
    val jsonTextFilter = jsonMetricDefinition.textFilters map { tuple =>
      val (path, words) = tuple
      path -> words.toSet
    }
    PredefinedMetricDefinitions.count(name = jsonMetricDefinition.name,
      pathsGroupBy = jsonMetricDefinition.groupBy.toSet,
      textFilters = jsonTextFilter.toMap,
      ifHas = Option(jsonMetricDefinition.ifHas),
      ifHasNot = Option(jsonMetricDefinition.ifHasNot))
  }
}

@JsonCreator
case class JsonMetricDefinition(
  @JsonProperty("name") name: String,
  @JsonProperty("groupBy") groupBy: Array[String],
  @JsonProperty("ifHas") ifHas: String,
  @JsonProperty("ifHasNot") ifHasNot: String,
  @JsonProperty("textFilters") textFilters: java.util.Map[String, Array[String]])