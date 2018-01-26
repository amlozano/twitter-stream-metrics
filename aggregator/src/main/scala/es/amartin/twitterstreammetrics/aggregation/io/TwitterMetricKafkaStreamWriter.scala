package es.amartin.twitterstreammetrics.aggregation.io

import com.typesafe.scalalogging.LazyLogging
import es.amartin.twitterstreammetrics.aggregation.aggregation._
import es.amartin.twitterstreammetrics.aggregation.io.conversions.{AggregationValueReducer,
InputToAggKeyAggValueMapper, WindowedToAggregatedValueMapper}
import es.amartin.twitterstreammetrics.aggregation.serdes.JSONSerde
import org.apache.kafka.streams.kstream._

import scala.concurrent.duration._

class TwitterMetricKafkaStreamWriter(
  metricDefinitionRetriever: MetricDefinitionRetriever,
  outputTopic: String,
  aggPeriod: Duration = 30.seconds
)
  extends StreamWriter[Integer, String] with LazyLogging {

  override def write(stream: KStream[Integer, String]): Unit = {
    stream
      .flatMap[AggregationKey, AggregationValue](new InputToAggKeyAggValueMapper(metricDefinitionRetriever))
      .groupByKey(new JSONSerde[AggregationKey], new JSONSerde[AggregationValue])
      .reduce(new AggregationValueReducer, TimeWindows.of(aggPeriod.toMillis))
      .toStream
      .map[AggregationKey, AggregatedValue](new WindowedToAggregatedValueMapper)
      .to(new JSONSerde[AggregationKey], new JSONSerde[AggregatedValue], outputTopic)

  }
}
