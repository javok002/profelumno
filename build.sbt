name := """profelumno"""

version := "1.0-SNAPSHOT"

lazy val common = (project in file("modules/common"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val users = (project in file("modules/users"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val teacherProfile = (project in file("modules/teacher-profile"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users)

lazy val register = (project in file("modules/register"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile, common)

lazy val delete = (project in file("modules/delete"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile, common)

lazy val teacherSubscription = (project in file("modules/teacher-subscription"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile, common)

lazy val hireLesson = (project in file("modules/hire-lesson"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherSearch, common)

lazy val teacherModification = (project in file("modules/teacher-modification"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile)

lazy val teacherSearch = (project in file("modules/teacher-search"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(register, common)

lazy val studentModification = (project in file("modules/student-modification"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users,register,common)

lazy val passwordRecovery = (project in file("modules/password-recovery"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users, mailSender, common)

lazy val mailSender = (project in file("modules/mail-sender"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val loginout = (project in file("modules/loginout"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(users, common)

lazy val architecture = (project in file("modules/architecture"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(teacherProfile, users, common)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(common,studentModification, users, teacherProfile, teacherSubscription, register, delete,
    passwordRecovery, mailSender, teacherModification, teacherSearch, hireLesson, architecture, loginout)
  .aggregate(common,studentModification, users, teacherProfile, teacherSubscription, register, delete,
    passwordRecovery, mailSender, teacherModification, teacherSearch, hireLesson, architecture,loginout)


scalaVersion := "2.11.6"

Common.settings

libraryDependencies ++= Common.dependencies

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

includeFilter in (Assets, LessKeys.less) := "*.less"


fork in run := true