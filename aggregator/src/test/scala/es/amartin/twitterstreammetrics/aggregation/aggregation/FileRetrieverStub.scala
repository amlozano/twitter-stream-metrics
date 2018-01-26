package es.amartin.twitterstreammetrics.aggregation.aggregation

class FileRetrieverStub(fileList: List[String], listException: Option[Throwable], contentException: Option[Throwable])
  extends FileRetriever {
  override def listContent(path: String): List[String] = listException match {
    case Some(e) => throw e
    case None => fileList
  }

  override def getContent(file: String): String = contentException match {
    case Some(e) => throw e
    case None => file
  }
}
