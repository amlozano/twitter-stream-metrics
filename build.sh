sbt clean && \
sbt twitter-ingestion/assembly && \
cp twitter-ingestion/target/scala-2.11/twitter-ingestion.jar twitter-ingestion/docker/twitter-ingestion.jar && \
cd twitter-ingestion/docker && \
docker build --no-cache -t twitter-ingestion . && \
cd ../.. && \
sbt aggregator/assembly && \
cp aggregator/target/scala-2.11/aggregator.jar aggregator/docker/aggregator.jar && \
cd aggregator/docker && \
docker build --no-cache -t aggregator . && \
cd ../.. && \
sbt kafka-to-influxdb/assembly && \
cp kafka-to-influxdb/target/scala-2.11/kafka-to-influxdb.jar kafka-to-influxdb/docker/kafka-to-influxdb.jar && \
cd kafka-to-influxdb/docker && \
docker build --no-cache -t kafka-to-influxdb .

