import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import play.sbt.PlayImport._
import sbt._
import sbt.Keys._

object Common {
  val settings: Seq[Setting[_]] = Seq(
    organization := "ua.dirpoy",
    version := "0.1-SNAPSHOT",
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node
  )

  val dependencies = Seq(
    javaJdbc,
    cache,
    "org.webjars.bower" % "angular" % "1.4.3"
  )
}