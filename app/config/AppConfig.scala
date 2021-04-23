package config

import javax.inject.Inject
import play.api.{Configuration, Environment}

class AppConfig@Inject()(runModeConfiguration: Configuration, environment: Environment) {

  lazy val base: String= runModeConfiguration.get[String]("locationService.base")

}
