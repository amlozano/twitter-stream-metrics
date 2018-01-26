package es.amartin.twitterstreammetrics.aggregation.io.conversions

import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.aggregation.aggregation.{AggregationKey, AggregationValue, AggregatedValue}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{KeyValueMapper, Windowed}

class WindowedToAggregatedValueMapper
  extends KeyValueMapper[Windowed[AggregationKey], AggregationValue, KeyValue[AggregationKey, AggregatedValue]]
    with LazyLogging {

  override def apply(
    key: Windowed[AggregationKey],
    value: AggregationValue): KeyValue[AggregationKey, AggregatedValue] = {
    logger.info("Converting...")
    new KeyValue[AggregationKey, AggregatedValue](
      key.key(), AggregatedValue(key.window.start(), key.window.end(), value))
  }

}
