package models

import play.api.libs.json.{Json, OFormat}

case class User(
  id:String,
  first_name:String,
  last_name:String,
  email: String,
  ip_address: String,
  latitude:Double,
  longitude:Double)

object User{
  implicit val format : OFormat[User] = Json.format
}
