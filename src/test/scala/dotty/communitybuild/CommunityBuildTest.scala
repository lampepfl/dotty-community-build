package dotty.communitybuild

import java.nio.file.{Path, Paths}

import org.junit.{Ignore, Test}

class CommunityBuildTest extends BuildTest {
  def testDirectory: Path =
    Paths.get(sys.props("user.dir") + "/out/")

  @Test def algebra = project(
    gitUrl = "https://github.com/dotty-staging/algebra",
    command = "coreJVM/compile"
  )

  @Test def betterfiles = project(
    gitUrl = "https://github.com/dotty-staging/better-files",
    command = "dottyCompile"
  )

  @Test def scalacheck = project(
    gitUrl = "https://github.com/dotty-staging/scalacheck",
    command = "jvm/compile"
  )

  @Test def ScalaPB = project(
    gitUrl = "https://github.com/dotty-staging/ScalaPB",
    command = "dottyCompile"
  )

  @Test def scalatest = project(
    gitUrl = "https://github.com/dotty-staging/scalatest",
    command = "scalatest/compile"
  )

  @Test def scopt = project(
    gitUrl = "https://github.com/dotty-staging/scopt",
    command = "scoptJVM/compile"
  )

  @Test def squants = project(
    gitUrl = "https://github.com/dotty-staging/squants",
    command = "squantsJVM/compile"
  )

  @Test def collectionstrawman = project(
    gitUrl = "https://github.com/dotty-staging/collection-strawman",
    command = "dottyCompile"
  )

  @Test def scalap = project(
    gitUrl = "https://github.com/dotty-staging/scala",
    branch  = "dotty-scalap",
    command = "scalap/compile"
  )
}
