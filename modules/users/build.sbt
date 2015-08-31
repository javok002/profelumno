name := """users"""

version := "1.0-SNAPSHOT"

lazy val admin = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

Common.settings

libraryDependencies ++= Common.dependencies