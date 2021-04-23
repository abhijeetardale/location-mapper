package connector

import config.AppConfig
import javax.inject.Inject
import exceptions._
import models.User
import play.api.http.Status._
import play.api.http.MimeTypes.JSON
import play.api.http.HttpErrorHandler
import play.api.libs.json.{JsResultException, JsValue}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserApiConnector@Inject()(wsClient: WSClient, appConfig: AppConfig) {

  def getUsers: Future[Either[Throwable, JsValue]] = {
    val serviceUrl = s"${appConfig.base}/getUsers"

    wsClient.url(serviceUrl).addHttpHeaders("Content-Type" -> JSON).get().map{ response =>

      response.status match {
        case OK => response.json.validate[User].fold(
          error => Left(throw new BadGatewayException(
            s"getUsers to $serviceUrl returned invalid JSON" +
              JsResultException(error).getMessage
          )),
          _ => Right(response.json)
        )
        case BAD_REQUEST => Left(throw new BadRequestException(response.body))
        case NOT_FOUND => Left(throw new NotFoundException(response.body))
        case INTERNAL_SERVER_ERROR => Left(throw new InternalServerException(response.body))
        case BAD_GATEWAY => Left(throw new BadGatewayException(response.body))
        case SERVICE_UNAVAILABLE => Left(throw new ServiceUnavailableException(response.body))
        case NOT_IMPLEMENTED => Left(throw new NotFoundException(response.body))
        case _ => Left(throw new UnrecognisedHttpResponseException("getUsers", serviceUrl, response))
      }

    }

  }

}
