FROM openjdk:8-jre

ADD twitter-ingestion.jar /opt/twitter-ingestion/twitter-ingestion.jar

CMD ["java", "-Dconfig.file=/opt/twitter-ingestion/application.conf", "-cp", "/opt/twitter-ingestion/twitter-ingestion.jar", "es.amartin.twitterstreammetrics.ingestion.TwitterIngestionApp"]
