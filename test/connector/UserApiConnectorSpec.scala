package connector

import com.github.tomakehurst.wiremock.client.WireMock._
import exceptions.InternalServerException
import org.apache.http.HttpException
import org.scalatest.{AsyncFlatSpec, EitherValues, OptionValues, RecoverMethods}
import org.scalatestplus.play.PortNumber
import play.api.libs.json.Json
import util.WireMockServerHelper

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class UserApiConnectorSpec extends WireMockServerHelper
  with OptionValues
  with RecoverMethods
  with EitherValues {

  val path = "/getUsers"

  "UserApiConnector" when {

    "executed for user list api defined by DWP" must {

      "return 200 with json body" in {

        stubFor(get(urlEqualTo(path))
          .withHeader("Content-Type", equalTo("application/json"))
          .willReturn(
            ok(Json.toJson("""{ [ "id" : "1" ]}""").toString())
              .withHeader("Content-Type", "application/json"))
        )

        val result = Await.result(inject[UserApiConnector].getUsers, Duration.Inf)

        result.right.get  mustBe Json.toJson("""{ [ "id" : "1" ]}""")

      }

      "throw exception fo BadRequestException (400)" in {

        stubFor(get(urlEqualTo(path))
          .withHeader("Content-Type", equalTo("application/json"))
          .willReturn(
            badRequest().withBody("Bad Request")
          )
        )

        intercept[InternalServerException](Await.result(inject[UserApiConnector].getUsers, Duration.Inf))
          .message mustBe "Bad Request"
      }

      "throw exception fo Internal Server Error" in {

        stubFor(get(urlEqualTo(path))
          .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(
              serverError.withBody("Internal Server Error")
            )
        )

        intercept[InternalServerException](Await.result(inject[UserApiConnector].getUsers, Duration.Inf))
          .message mustBe "Internal Server Error"

      }

      "throw exception fo internal server error" in {

        stubFor(get(urlEqualTo(path))
          .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(
              serverError.withBody("internal server error")
            )
        )

        intercept[InternalServerException](Await.result(inject[UserApiConnector].getUsers, Duration.Inf))
          .message mustBe "internal server error"

      }

//      "throw exception for not valid http response code" in {
//
//        server.stubFor(
//          method
//            .willReturn(
//              aResponse().withStatus(600)
//            )
//        )
//
//        recoverToExceptionIf[Exception] (call) map {
//          ex =>
//            ex.getMessage should include("failed with status")
//        }
//      }

    }

  }

}
