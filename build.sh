sbt clean && \
sbt twitter-ingestion/assembly && \
cp twitter-ingestion/docker/Dockerfile twitter-ingestion/target/scala-2.12/Dockerfile && \
cd twitter-ingestion/target/scala-2.12/ && \
docker build --no-cache -t twitter-ingestion . && \
cd ../../.. && \
sbt aggregator/assembly && \
cp aggregator/docker/Dockerfile aggregator/target/scala-2.12/Dockerfile && \
cd aggregator/target/scala-2.12 && \
docker build --no-cache -t aggregator . && \
cd ../../.. && \
sbt kafka-to-influxdb/assembly && \
cp kafka-to-influxdb/docker/Dockerfile kafka-to-influxdb/target/scala-2.12/Dockerfile && \
cd kafka-to-influxdb/target/scala-2.12 && \
docker build --no-cache -t kafka-to-influxdb .

