name := """student-profile"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

Common.settings

playEbeanModels in Compile := Seq("ua.dirproy.profelumno.studentprofile.models.*")

libraryDependencies ++= Common.dependencies