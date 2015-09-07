name := """profelumno"""

version := "1.0-SNAPSHOT"

lazy val users = (project in file("modules/users"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val teacherProfile = (project in file("modules/teacher-profile"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users, teacherProfile)
  .aggregate(users, teacherProfile)

scalaVersion := "2.11.6"

Common.settings

libraryDependencies ++= Common.dependencies

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


