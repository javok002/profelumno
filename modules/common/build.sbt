name := """common"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

watchSources ++= (watchSources.value --- baseDirectory.value / "modules/common/app/assets" ** "*" --- baseDirectory.value / "public" ** "*").get

Common.settings

playEbeanModels in Compile := Seq("ua.dirproy.profelumno.common.models.*")

libraryDependencies ++= Common.dependencies
