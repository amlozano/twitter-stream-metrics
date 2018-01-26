package es.amartin.twitterstreammetrics.toinflux.serdes

import java.lang.reflect.{ParameterizedType, Type}
import java.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

object Json {

  type ParseException = JsonParseException
  type NonRecognizedPropertyException = UnrecognizedPropertyException

  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

  private def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType: Type = typeFromManifest(manifest[T])
  }

  private def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {
      m.runtimeClass
    }
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

  object ByteArray {
    def encode(value: Any): Array[Byte] = mapper.writeValueAsBytes(value)

    def decode[T: Manifest](value: Array[Byte]): T = mapper.readValue(value, typeReference[T])
  }

}

class JSONSerializer[T] extends Serializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] =
    Json.ByteArray.encode(data)

  override def close(): Unit = ()
}

class JSONDeserializer[T >: Null <: Any : Manifest] extends Deserializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T = Option(data).map(Json.ByteArray.decode[T]).orNull
}

class JSONSerde[T >: Null <: Any : Manifest] extends Serde[T] {
  override def deserializer(): Deserializer[T] = new JSONDeserializer[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def serializer(): Serializer[T] = new JSONSerializer[T]
}