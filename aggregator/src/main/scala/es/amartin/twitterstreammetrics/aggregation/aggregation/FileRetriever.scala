package es.amartin.twitterstreammetrics.aggregation.aggregation

import java.io.File

import scala.io.Source

trait FileRetriever {
  def listContent(path: String): List[String]
  def getContent(file: String): String
}

class LocalFileRetriever extends FileRetriever {
  override def listContent(path: String): List[String] = new File(path).listFiles.map(_.getAbsolutePath).toList

  override def getContent(file: String): String = Source.fromFile(file).getLines.mkString
}