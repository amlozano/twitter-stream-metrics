package es.amartin.twitterstreammetrics.aggregation.aggregation

import java.io.FileNotFoundException

import org.scalatest.{FunSpec, Matchers}

class FileMetricDefinitionRetrieverSpec extends FunSpec with Matchers {

  private def fixture(
    fileList: List[String],
    fallbackRetriever: Option[MetricDefinitionRetriever] = None,
    listException: Option[Throwable] = None,
    contentException: Option[Throwable] = None) = new {
    val metricParserStub = new MetricParserStub
    val fileRetrieverStub = new FileRetrieverStub(fileList, listException, contentException)
    val unit = new FileMetricDefinitionRetriever("dir", metricParserStub, fileRetrieverStub, fallbackRetriever)
  }

  describe("A FileMetricDefinitionRetriever") {
    it("should return empty set if there are no files in the directory") {
      val f = fixture(List())

      f.unit.get shouldBe Set()
    }

    it("should return a set of MetricDefinition when there are files in the directory") {
      val f = fixture(List("file1", "file2"))

      val result = f.unit.get

      result.size shouldBe 2
      result.head.name shouldBe "file1"
      result.last.name shouldBe "file2"
    }

    it("should return fallback set when there is an exception in listing the files") {
      val f = fixture(
        fileList = List(),
        listException = Some(new FileNotFoundException()),
        fallbackRetriever = Some(new MetricDefinitionRetrieverStub(List("stubMetric"))))

      val result = f.unit.get

      result.size shouldBe 1
      result.head.name shouldBe "stubMetric"
    }

    it("should return fallback set when there is an exception in getting the content of a file") {
      val f = fixture(
        fileList = List("file1"),
        contentException = Some(new SecurityException()),
        fallbackRetriever = Some(new MetricDefinitionRetrieverStub(List("stubMetric"))))

      val result = f.unit.get

      result.size shouldBe 1
      result.head.name shouldBe "stubMetric"
    }

    it("should throw exception when there is an exception in listing files and no fallback retriever") {
      val f = fixture(
        fileList = List(),
        listException = Some(new SecurityException()))

      intercept[SecurityException] {
        f.unit.get
      }
    }

    it("should throw exception when there is an exception in getting the content of a file and no fallback retriever") {
      val f = fixture(
        fileList = List("file1", "file2"),
        contentException = Some(new SecurityException()))

      intercept[SecurityException] {
        f.unit.get
      }
    }
  }

}
