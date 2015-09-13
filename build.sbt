name := """profelumno"""

version := "1.0-SNAPSHOT"

lazy val users = (project in file("modules/users"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val teacherProfile = (project in file("modules/teacher-profile"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users)

lazy val register = (project in file("modules/register"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val delete = (project in file("modules/delete"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val teacherSubscription = (project in file("modules/teacher-subscription"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val teacherModification = (project in file("modules/teacher-modification"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val teacherSearch = (project in file("modules/teacher-search"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val passwordRecovery = (project in file("modules/password-recovery"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users)

lazy val mailSender = (project in file("modules/mail-sender"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users, teacherProfile, teacherSubscription, register, delete, passwordRecovery, mailSender,teacherModification, teacherSearch)
  .aggregate(users, teacherProfile, teacherSubscription, register, delete, passwordRecovery, mailSender,teacherModification, teacherSearch)

scalaVersion := "2.11.6"

Common.settings

libraryDependencies ++= Common.dependencies

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
