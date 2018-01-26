package es.amartin.twitterstreammetrics.aggregation.aggregation

object DummyMetricDefinition {

  def apply(value: String): MetricDefinition = MetricDefinition(_ => true, value, _ => Map(), 0, value)

}
