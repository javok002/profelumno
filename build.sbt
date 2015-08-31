name := """profelumno"""

version := "1.0-SNAPSHOT"

lazy val admin = (project in file("modules/users")).enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .dependsOn(admin)
  .aggregate(admin)

scalaVersion := "2.11.6"

Common.settings

libraryDependencies ++= Common.dependencies

target in bower := baseDirectory.value / "app" / "assets"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


