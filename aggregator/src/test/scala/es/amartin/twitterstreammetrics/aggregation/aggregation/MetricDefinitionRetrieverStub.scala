package es.amartin.twitterstreammetrics.aggregation.aggregation

import org.scalatest.Matchers

class MetricDefinitionRetrieverStub(results: List[String])
  extends MetricDefinitionRetriever with Matchers {

  private var resultIndex = 0

  override def get: Set[MetricDefinition] = {
    if (resultIndex >= results.size) fail("MetricDefinitionRetriever called too many times")
    val result = results(resultIndex)
    resultIndex += 1
    Set(DummyMetricDefinition(result))
  }
}
