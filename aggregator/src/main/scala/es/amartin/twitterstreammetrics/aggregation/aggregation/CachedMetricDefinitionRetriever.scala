package es.amartin.twitterstreammetrics.aggregation.aggregation

import java.util.Calendar

import scala.concurrent.duration._

class CachedMetricDefinitionRetriever(
  metricDefinitionRetriever: MetricDefinitionRetriever,
  retention: Duration = 10.seconds)
  extends MetricDefinitionRetriever {
  private var cache = Set[MetricDefinition]()
  private var lastUpdateTime = 0L

  private def updateCache() = try {
    cache = metricDefinitionRetriever.get
  } finally {
    lastUpdateTime = Calendar.getInstance.getTimeInMillis
  }

  override def get: Set[MetricDefinition] = {
    if (Calendar.getInstance.getTimeInMillis > (lastUpdateTime + retention.toMillis)) {
      updateCache()
    }
    cache
  }
}
