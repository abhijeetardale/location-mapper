package util

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import play.api.test.Injecting

trait WireMockServerHelper extends PlaySpec with GuiceOneAppPerSuite with Injecting with BeforeAndAfterAll with BeforeAndAfterEach {
  this: Suite =>

  val stubPort = 8080
  val stubHost = "localhost"

  val wireMockServer: WireMockServer = new WireMockServer(wireMockConfig().port(stubPort))


  implicit lazy val client: WSClient = inject[WSClient]

  override lazy val app: Application = new GuiceApplicationBuilder().configure(
    "locationService.base" -> s"http://$stubHost:$stubPort").build()

  override def beforeAll(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(stubHost, stubPort)
  }

  override def afterAll(): Unit = wireMockServer.stop()

  override def beforeEach(): Unit = WireMock.reset()
}
