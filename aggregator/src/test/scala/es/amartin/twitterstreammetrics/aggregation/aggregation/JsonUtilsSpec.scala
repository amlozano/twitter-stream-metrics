package es.amartin.twitterstreammetrics.aggregation.aggregation

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.{FunSpec, Matchers}
import collection.JavaConversions._

class JsonUtilsSpec extends FunSpec with Matchers {

  private def fixture = new {
    val mapper = new ObjectMapper
    val jsonText = """{"key1":"value1", "key2": {"nestedKey2": "value2"}}"""
    val jsonNode: JsonNode = mapper.readTree(jsonText)
    val correctMetricDefinitionString: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"inSomeField":["filterThis","andAlsoThis"]}
        |}""".stripMargin
    val jsonMetricDefinition = JsonMetricDefinition(
      name = "name",
      groupBy = Array("groupBy", "andBy"),
      textFilters = Map("text" -> Array("filterThis", "andAlsoThis")),
      ifHas = null,
      ifHasNot = null)
  }

  describe("A JsonUtil") {
    it("should extract a single path from a JsonNode") {
      val f = fixture

      val result = JsonUtils.fromPath(f.jsonNode, "key1")

      result.asText shouldBe "value1"
    }

    it("should extract a complex path from a JsonNode") {
      val f = fixture

      val result = JsonUtils.fromPath(f.jsonNode, "key2.nestedKey2")

      result.asText shouldBe "value2"
    }

    it("should return empty string if the path is not defined") {
      val f = fixture

      val result = JsonUtils.fromPath(f.jsonNode, "wrongKey")

      result.asText shouldBe ""
    }

    it("should parse correct metric definition") {
      val f = fixture

      val result = JsonUtils.to(f.correctMetricDefinitionString, classOf[JsonMetricDefinition])

      result.textFilters.keySet.size shouldBe 1
      result.textFilters.get("inSomeField").length shouldBe 2
      result.textFilters.get("inSomeField").head shouldBe "filterThis"
      result.textFilters.get("inSomeField").last shouldBe "andAlsoThis"
      result.name shouldBe f.jsonMetricDefinition.name
      result.groupBy shouldBe f.jsonMetricDefinition.groupBy
    }

    it("should throw JsonParseException with wrong metric definition") {
      intercept[JsonParseException] {
        JsonUtils.to("hello, I am a wrong json", classOf[JsonMetricDefinition])
      }
    }
  }

}
