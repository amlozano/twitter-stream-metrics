package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.typesafe.scalalogging.LazyLogging

class FileMetricDefinitionRetriever(
  directoryPath: String,
  metricDefinitionParser: MetricDefinitionParser,
  fileRetriever: FileRetriever,
  fallbackRetriever: Option[MetricDefinitionRetriever] = None)
  extends MetricDefinitionRetriever with LazyLogging {

  override def get: Set[MetricDefinition] = try {
    val files = fileRetriever.listContent(directoryPath)
    files
      .map(fileRetriever.getContent)
      .map(metricDefinitionParser.parse).toSet
  } catch {
    case e: Exception => fallbackRetriever map { retriever: MetricDefinitionRetriever =>
      logger.warn(s"Exception thrown '${e.getMessage}' when trying to retrieve metrics from '$directoryPath'")
      logger.warn(s"falling back to fallback retriever")
      retriever.get
    } getOrElse {
      logger.warn(s"Exception thrown '${e.getMessage}' when trying to retrieve metrics from '$directoryPath'")
      throw e
    }
  }
}
