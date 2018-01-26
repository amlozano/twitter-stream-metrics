package es.amartin.twitterstreammetrics.aggregation.aggregation

trait MetricDefinitionRetriever {
  def get: Set[MetricDefinition]
}
