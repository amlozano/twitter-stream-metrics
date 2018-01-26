
package es.amartin.twitterstreammetrics.ingestion.dest

import org.apache.kafka.clients.producer.ProducerRecord

import scala.util.Random

trait RecordGenerator[I, K, V] {
  def generateRecord(input: I): ProducerRecord[K, V]
}

class MessageToOnlyByteValueRecordGenerator(topic: String) extends RecordGenerator[String, Int, Array[Byte]] {
  private val randomGenerator = new Random
  override def generateRecord(input: String): ProducerRecord[Int, Array[Byte]] =
    new ProducerRecord[Int, Array[Byte]](topic, randomGenerator.nextInt, input.getBytes)
}