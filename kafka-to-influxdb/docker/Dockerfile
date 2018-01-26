FROM openjdk:8-jre

ADD kafka-to-influxdb.jar /opt/kafkaToInflux/kafka-to-influxdb.jar

CMD ["java", "-cp", "/opt/kafkaToInflux/kafka-to-influxdb.jar", "es.amartin.twitterstreammetrics.toinflux.KafkaToInfluxApp"]
