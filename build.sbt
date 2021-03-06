import java.nio.file.Files

import play.core.PlayVersion
import sbt.{AllPassFilter, PathFinder}

import scala.sys.process.ProcessLogger

name := """e14_verification"""
organization := "rysoft"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.12.3"

(scalacOptions in ThisBuild) ++= Seq("-Xmax-classfile-name", "130")

// Scala
libraryDependencies ++= Seq(
  guice,
  ws,
  jdbc,
  ehcache,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  "org.scalaz" %% "scalaz-core" % "7.2.23",
// Java
  "org.postgresql" % "postgresql" % "42.1.1",
  //"com.twitter" % "joauth" % "6.0.2",
  //JWT
  "com.pauldijou" %% "jwt-play" % "0.16.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13"
)

pipelineStages := Seq(digest)