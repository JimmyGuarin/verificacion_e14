name := """dream-commands"""
organization := "dream-team"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SwaggerPlugin)

swaggerDomainNameSpaces := Seq("models")

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.1"
libraryDependencies += jdbc

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.1"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.15"

libraryDependencies += "org.webjars" % "swagger-ui" % "2.2.10"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "dream-team.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "dream-team.binders._"
