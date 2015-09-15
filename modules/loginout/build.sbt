name := """loginout"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

Common.settings

playEbeanModels in Compile := Seq("ua.dirproy.profelumno.loginout.models.*")

libraryDependencies ++= Common.dependencies