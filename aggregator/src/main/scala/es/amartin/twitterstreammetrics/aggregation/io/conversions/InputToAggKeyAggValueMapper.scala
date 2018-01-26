package es.amartin.twitterstreammetrics.aggregation.io.conversions

import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.aggregation.aggregation._
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KeyValueMapper

import collection.JavaConverters._

class InputToAggKeyAggValueMapper(metricRetriever: MetricDefinitionRetriever)
  extends KeyValueMapper[Integer, String, java.util.Set[KeyValue[AggregationKey,
    AggregationValue]]] with LazyLogging {
  override def apply(
    key: Integer,
    value: String): java.util.Set[KeyValue[AggregationKey, AggregationValue]] = {
    val jsonMessage = JsonUtils.toJsonNode(value)
    metricRetriever.get
      .flatMap(metric =>
        if (metric.filter(jsonMessage)) {
          val res = Set[KeyValue[AggregationKey, AggregationValue]](
            new KeyValue[AggregationKey, AggregationValue](
              AggregationKey(metric.aggregationFunction, metric.name, metric.fieldExtractor(jsonMessage)),
              AggregationValue(jsonMessage, metric)))
          res
        } else {
          Set[KeyValue[AggregationKey, AggregationValue]]()
        }).asJava
  }
}
