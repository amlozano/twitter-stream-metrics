package es.amartin.twitterstreammetrics.aggregation.io.conversions

import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.aggregation.aggregation.AggregationValue
import es.amartin.twitterstreammetrics.aggregation.aggregation.functions.AggregationFunction
import org.apache.kafka.streams.kstream.Reducer

class AggregationValueReducer extends Reducer[AggregationValue] with LazyLogging {

  override def apply(
    value1: AggregationValue,
    value2: AggregationValue): AggregationValue = {
    logger.info(s"Reducing values: $value1, $value2")
    AggregationValue(
      AggregationFunction(value1.aggregationFunction)
        .function(value1.value, value2.value), value1.aggregationFunction)
  }

}
