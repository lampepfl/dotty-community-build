package dotty.communitybuild

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.text.SimpleDateFormat
import java.util.Date
import org.junit.Assert._
import org.junit.Test

case class CommunityProject(name: String) {
  def workingDirectory: Path =
    Paths.get(sys.props("user.dir")).getParent.resolve(name)
}

abstract class CommunityProjectTest(project: CommunityProject) {

  private val NightlyDate = ".*bin-(\\d+)-.*".r
  private val dateFormat = new SimpleDateFormat("yyyyMMdd")
  private val iso8601 = new SimpleDateFormat("yyyy-MM-dd")
  private def getDate(nightlyVersion: String): Date = {
    nightlyVersion match {
      case NightlyDate(date) => dateFormat.parse(date)
      case els =>
        sys.error(
          s"Unknown date format $els. Expected ${dateFormat.toPattern}")
    }
  }
  private val day = 60L * 60 * 24 * 1000
  private val days = 3 * day

  // query latest dotty commits from GitHub api.
  private def commitsInLastDays: String = {
    val daysAgo = new Date(new Date().getTime - days)
    val since = iso8601.format(daysAgo)
    scala.io.Source.fromURL(
      s"https://api.github.com/repos/lampepfl/dotty/commits?since=$since"
    ).mkString
  }

  def assertVersionIsUpToDate(nightlyVersion: String): Unit = {
    val nightly = getDate(nightlyVersion)
    val now = new Date()
    val diff = now.getTime - nightly.getTime
    assert(
      diff < days || commitsInLastDays == "[]" ,
      s"Outdated version $nightlyVersion"
    )
  }

  private def log(msg: String) = {
    val banner = "*" * (msg.length + 5)
    println(banner)
    println("===> " + msg)
    println(banner)
  }
  log(s"Using dotty version ${BuildInfo.dottyVersion}")
  assertVersionIsUpToDate(BuildInfo.dottyVersion)
  private val SbtCommand = ".*sbt ([^ ]+).*".r
  @Test def compilesWithDotty(): Unit = {
    log(s"Starting....")
    // By convention, run the first command passed to sbt in .travis.yml
    val sbtCommand = new String(Files.readAllBytes(
      project.workingDirectory.resolve(".travis.yml"))).lines
      .sliding(2)
      .collectFirst {
        case "script:" :: SbtCommand(cmd) :: Nil => cmd
      }
      .getOrElse {
        throw new IllegalStateException(
          s"Unable to infer sbt command to test project $project")
      }

    def exec(binary: String, arguments: String*): Unit = {
      val command = binary +: arguments
      log(s"Running command: ${command.mkString(" ")}")

      import scala.collection.JavaConverters._
      val builder = new ProcessBuilder(command.asJava)
        .directory(project.workingDirectory.toFile)
        .inheritIO()

      val process = builder.start()
      val exit = process.waitFor()
      assertEquals(exit, 0)
    }
    exec("git", "clean", "-xfd")
    exec("git", "checkout", "dotty")
    exec("git", "pull", "origin", "dotty")
    exec("sbt", s"++${BuildInfo.dottyVersion}", sbtCommand)
    log(s"DONE!")
  }
}

class Scalatest extends CommunityProjectTest(CommunityProject("scalatest"))

class Squants extends CommunityProjectTest(CommunityProject("squants"))

class Algebra extends CommunityProjectTest(CommunityProject("algebra"))

class ScalaPB extends CommunityProjectTest(CommunityProject("ScalaPB"))

class Scopt extends CommunityProjectTest(CommunityProject("scopt"))
