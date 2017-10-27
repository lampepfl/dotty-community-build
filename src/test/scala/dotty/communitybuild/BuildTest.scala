package dotty.communitybuild

import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

import org.junit.Assert.assertEquals

abstract class BuildTest {

  protected def testDirectory: Path

  /** Default Dotty version is defined in `build.sbt` */
  protected def dottyVersion: String = BuildInfo.dottyVersion

  /** Build the given project with Dotty version: [[dottyVersion]]
    *
    * @param gitUrl  A link to the project's git repository
    * @param branch  The name of the branch to build. Default is `dotty`
    * @param command The sbt command used to build the project
    */
  final def project(gitUrl: String, branch: String = "dotty", command: String): Unit = {
    def log(msg: String) = println(Console.GREEN + msg + Console.RESET)

    // recursively delete a directory if it exists
    def deleteDir(dir: Path): Unit = if (Files.exists(dir)) {
      Files.walkFileTree(dir, new SimpleFileVisitor[Path]() {
        override def visitFile(file: Path, attrs: BasicFileAttributes) = {
          Files.delete(file)
          FileVisitResult.CONTINUE
        }
        override def postVisitDirectory(dir: Path, exc: IOException) = {
          Files.delete(dir)
          FileVisitResult.CONTINUE
        }
      })
    }

    val name = gitUrl.drop(gitUrl.lastIndexOf('/') + 1)
    log(s"Building $name with Dotty $dottyVersion...")

    // create a working directory: testDirectory/projectName
    val projectDir = testDirectory.resolve(name)
    deleteDir(projectDir)
    Files.createDirectories(projectDir)

    // execute shell command
    def exec(binary: String, arguments: String*) = {
      val command = binary +: arguments
      log(command.mkString(" "))

      import scala.collection.JavaConverters._
      val builder = new ProcessBuilder(command.asJava)
        .directory(projectDir.toFile)
        .inheritIO()

      val process = builder.start()
      val exit = process.waitFor()
      assertEquals(exit, 0)
    }

    // clone repository and run sbt command with the dottyVersion set in the build
    exec("git", "clone", "-b", branch, gitUrl, ".")
    exec("sbt", s"++$dottyVersion", command)
  }
}
