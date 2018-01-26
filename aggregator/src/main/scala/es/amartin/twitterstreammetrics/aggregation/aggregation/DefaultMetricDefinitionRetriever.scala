package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

import scala.concurrent.duration._

class DefaultMetricDefinitionRetriever(aggTime: Duration = 30.seconds) extends MetricDefinitionRetriever {
  override def get: Set[MetricDefinition] = Set(
    PredefinedMetricDefinitions
      .count(textFilters = Map("text" -> Set("malaga", "málaga")), name = "containsMalaga"),
    PredefinedMetricDefinitions.count(name = "count"),
    PredefinedMetricDefinitions
      .count(name = "byTimeZone", pathsGroupBy = Set("user.time_zone")),
    PredefinedMetricDefinitions
      .count(name = "byLang", pathsGroupBy = Set("user.lang")),
    PredefinedMetricDefinitions
      .count(name = "bySource", pathsGroupBy = Set("source")),
    PredefinedMetricDefinitions
      .count(textFilters = Map("text" -> Set("catalonia", "cataluña", "catalunya")),
        name = "containsCataloniaByTimeZone",
        pathsGroupBy = Set("user.time_zone"))
  )
}

object PredefinedMetricDefinitions {

  private def correctEmpty(text: String): String = if (text.isEmpty) "#EMPTY#" else text

  def count(
    name: String,
    pathsGroupBy: Set[String] = Set(),
    textFilters: JsonTextFilter = Map(),
    ifHas: Option[String] = None,
    ifHasNot: Option[String] = None): MetricDefinition =
    MetricDefinition(
      filter = toJsonFilter(textFilters, ifHas, ifHasNot),
      aggregationFunction = "count",
      fieldExtractor = node => pathsGroupBy.map(path => path -> correctEmpty(JsonUtils.fromPath(node, path).asText))
        .toMap,
      defaultValue = 0,
      name = name
    )

  private def toJsonFilter(filterWords: JsonTextFilter, ifHas: Option[String], ifHasNot: Option[String]): JsonFilter = {
    node =>
      ifHas.forall(key => !JsonUtils.fromPath(node, key).isMissingNode) &&
        ifHasNot.forall(key => JsonUtils.fromPath(node, key).isMissingNode) &&
        (filterWords.isEmpty ||
          (filterWords forall { tuple =>
            val (path, words) = tuple
            words.exists(JsonUtils.fromPath(node, path).asText().toLowerCase().contains)
          }))
  }

}

object JsonUtils {

  private val mapper = new ObjectMapper

  def toJsonNode(message: String): JsonNode = mapper.readTree(message)

  def fromPath(node: JsonNode, path: String): JsonNode = {
    val pathElements = path.split('.')
    pathElements.foldLeft(node)((node, pathElement) => {
      node.path(pathElement)
    })
  }

  def to[T](message: String, valueType: Class[T]): T = {
    mapper.readValue[T](message, valueType)
  }

}
