package es.amartin.twitterstreammetrics.aggregation.aggregation

class MetricParserStub extends MetricDefinitionParser {
  override def parse(metricDefinition: String): MetricDefinition = DummyMetricDefinition(metricDefinition)
}
