package dotty.communitybuild

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Assert._
import org.junit.Test

case class CommunityProject(
    name: String,
    sbtCommands: List[String] = List("clean", "compile")
) {
  def workingDirectory: Path =
    Paths.get(sys.props("user.dir")).getParent.resolve(name)
}

object DottyVersion {
  // NOTE: the "latest" version in the maven-metadata.xml points to a release from
  // January because that version has no PATCH (0.1-201701XXX) while the actual latest
  // releases have a PATCH version (0.1.1-2017XXXX). Until 0.2 is released,
  // we sort by the date (2017XXXX) part and take the latest one by lexicographical order.
  case class NightlyVersion(monthDay: String, version: String)
  private val MonthDay = """.*2017(\d\d\d\d).*""".r
  private val Version = """      <version>(.*)</version>""".r
  def latestDottyNightlyRelease: Option[String] =
    scala.io.Source
      .fromURL(
        "http://repo1.maven.org/maven2/ch/epfl/lamp/dotty_2.11/maven-metadata.xml")
      .getLines()
      .collect {
        case Version(version @ MonthDay(monthDay)) =>
          NightlyVersion(monthDay, version)
      }
      .toSeq
      .sortBy(_.monthDay)
      .lastOption
      .map(_.version)
  lazy val latest: String = sys.env.getOrElse(
    "DOTTY_VERSION",
    latestDottyNightlyRelease.getOrElse {
      throw new IllegalStateException(
        "Unable to automatically fetch latest dotty nightly release, " +
          "please manually pass the environment variable DOTTY_VERSION.")
    }
  )
}

abstract class CommunityProjectTest(project: CommunityProject) {
  def log(msg: String) = {
    val banner = "*" * (msg.length + 5)
    println(banner)
    println("===> " + msg)
    println(banner)
  }
  log(s"Using dotty version ${DottyVersion.latest}")
  @Test def compilesWithDotty(): Unit = {
    log(s"Starting....")
    def exec(binary: String, arguments: String*): Unit = {
      val command = binary +: arguments
      log(s"Running command: ${command.mkString(" ")}")

      import scala.collection.JavaConverters._
      val builder = new ProcessBuilder(command.asJava)
        .directory(project.workingDirectory.toFile)
        .inheritIO()
      builder
        .environment()
        .put("COMPILERVERSION", DottyVersion.latest)

      val process = builder.start()
      val exit = process.waitFor()
      assertEquals(exit, 0)
    }
    exec("git", "clean", "-xfd")
    exec("git", "checkout", "dotty")
    exec("git", "pull", "origin", "dotty")
    exec("sbt", project.sbtCommands: _*)
    log(s"DONE!")
  }
}

class Scalatest extends CommunityProjectTest(CommunityProject("scalatest"))

class Squants extends CommunityProjectTest(CommunityProject("squants"))

class Algebra extends CommunityProjectTest(CommunityProject("algebra", List("coreJVM/compile")))
