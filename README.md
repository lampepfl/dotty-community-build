# Dotty Community Build
[![Community Build Status](https://travis-ci.org/lampepfl/dotty-community-build.svg?branch=master)](https://travis-ci.org/lampepfl/dotty-community-build)

This repository contains tests to build a corpus of Scala open sources projects
against a specific version of Dotty.

To run the community build on a local machine, clone the repo and execute `./run.sh`.

The tests will by default run against the latest NIGHTLY build of dotty.
You may customize the dotty bersion by passing the `DOTTY_REFERENCE` variable to `run.sh`. This
variable is used to identify a specific revision to build, publish locally and against which the
tests will be run. Some examples:

  - Build a PR:
    ```sh
    DOTTY_REFERENCE="+refs/pull/3306/merge" ./run.sh
    ```
  - Build a tag:
    ```sh
    DOTTY_REFERENCE="0.3.0-RC2" ./run.sh
    ```
  - Build a specific commit:
    ```sh
    DOTTY_REFERENCE="deadbeef" ./run.sh
    ```
  - Build using the latest nightly:
    ```sh
    ./run.sh
    ```

## Adding your project
To add your project to the community build you can follow these steps:

1. Create a new branch in your project. Name it `dotty` (not mandatory).

2. Get your project to compile with Dotty. Instructions can be found on the [dotty-example-project](https://github.com/lampepfl/dotty-example-project).
   Here are examples of projects that compile with Dotty:
     - [typelevel/algebra](https://github.com/dotty-staging/algebra/commits/dotty)
     - [pathikrit/better-files](https://github.com/dotty-staging/better-files/commits/dotty)
     - [rickynils/scalacheck](https://github.com/dotty-staging/scalacheck/commits/dotty)
     - [scalapb/ScalaPB](https://github.com/dotty-staging/ScalaPB/commits/dotty)
     - [scalatest/scalatest](https://github.com/dotty-staging/scalatest/commits/dotty)
     - [scopt/scopt](https://github.com/dotty-staging/scopt/commits/dotty)
     - [typelevel/squants](https://github.com/dotty-staging/squants/commits/dotty)
     - [scala/collection-strawman](https://github.com/dotty-staging/collection-strawman/commits/dotty)

   Please [open an issue](https://github.com/lampepfl/dotty/issues/new) on Github if you believe your
   project doesn't compile because of a bug in Dotty.

3. Once your project compiles with Dotty, open a PR against this repo that:
     - adds a test for your project in [CommunityBuildTest.scala](https://github.com/lampepfl/dotty-community-build/blob/master/src/test/scala/dotty/communitybuild/CommunityBuildTest.scala).
       A test requires:
         - Your project git url
         - The name of the branch to build. Default to `dotty`
         - The command `sbt` needs to run in order to build the project
     - adds an entry for your project in [.travis.yml](https://github.com/lampepfl/dotty-community-build/blob/master/.travis.yml) that matches the name of the test
