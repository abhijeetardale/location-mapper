package exceptions


import play.api.http.Status._
import play.api.libs.ws.WSResponse

class HttpException(val message: String, val responseCode: Int) extends Exception(message)

//400s
class BadRequestException(message: String) extends HttpException(message, BAD_REQUEST)

class NotFoundException(message: String) extends HttpException(message, NOT_FOUND)


//500s
class InternalServerException(message: String) extends HttpException(message, INTERNAL_SERVER_ERROR)

class NotImplementedException(message: String) extends HttpException(message, NOT_IMPLEMENTED)

class BadGatewayException(message: String) extends HttpException(message, BAD_GATEWAY)

class ServiceUnavailableException(message: String) extends HttpException(message, SERVICE_UNAVAILABLE)

class UnrecognisedHttpResponseException(method: String, url: String, response: WSResponse)
  extends Exception(s"$method to $url failed with status ${response.status}. Response body: '${response.body}'")
