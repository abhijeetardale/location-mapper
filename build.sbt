import play.sbt.PlayImport._

name := """location-mapper"""

version := "1.0-SNAPSHOT"

val compile = Seq(ws)

val test = Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.24.5" % Test,
  "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0" % Test
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
  libraryDependencies ++= compile ++ test,
  parallelExecution in Test := false,
  fork in Test := false,
  retrieveManaged := true,
  scalaVersion := "2.11.12"
)

