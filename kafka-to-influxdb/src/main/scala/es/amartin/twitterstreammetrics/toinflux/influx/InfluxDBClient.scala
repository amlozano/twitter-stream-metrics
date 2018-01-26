package es.amartin.twitterstreammetrics.toinflux.influx

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl._

import com.squareup.okhttp.OkHttpClient
import com.typesafe.scalalogging.LazyLogging
import org.influxdb.{InfluxDB, InfluxDBFactory}
import org.influxdb.dto.{BatchPoints, Point}
import retrofit.client.OkClient

trait InfluxDBClient {
  def writeItems(database: String, retentionPolicy: String, points: Iterable[Point])
}

class InfluxDBClientImpl(host: String, user: String, password: String) extends InfluxDBClient with LazyLogging {

  private object naiveTrustManager extends X509TrustManager with Serializable {
    override def getAcceptedIssuers: Array[X509Certificate] = {
      new Array[X509Certificate](0)
    }

    override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

    override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}
  }

  private object naiveHostnameVerifier extends HostnameVerifier with Serializable {
    override def verify(s: String, sslSession: SSLSession): Boolean = true
  }

  private val httpClient = new OkHttpClient()
    .setSslSocketFactory(socketFactory)
    .setHostnameVerifier(naiveHostnameVerifier)
  private val client = new OkClient(httpClient)

  private val influxDb = InfluxDBFactory.connect(host, user, password, client)

  private def socketFactory = {
    val context = SSLContext.getInstance("SSL")
    context.init(null, Array(naiveTrustManager), new SecureRandom())
    context.getSocketFactory
  }

  override def writeItems(
    database: String,
    retentionPolicy: String,
    points: Iterable[Point]): Unit = {
    val batchPoints  = BatchPoints.database(database).build()
    points.foreach(batchPoints.point)
    logger.info("Writing %d points to InfluxDB...".format(batchPoints.getPoints.size()))
    influxDb.write(batchPoints)
  }
}
