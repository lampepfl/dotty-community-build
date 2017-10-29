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
2. Edit the build to use the `sbt-dotty` plugin. Examples:
    - https://github.com/dotty-staging/better-files/commit/6ed1b74d338908f87dab251d11812868a5fd7e4b
    - https://github.com/dotty-staging/squants/commit/731c8874295a6aca609113ffb07e50cb12b58381
    - https://github.com/dotty-staging/scalatest/commit/90969923f21841e0f57f00bcf81481e14c8685eb
3. Make the project compile with Dotty. Please [open an issue](https://github.com/lampepfl/dotty/issues/new)
on Github if you think your project doesn't compile because of a bug in Dotty. Example migration
steps to make a project compile with Dotty: https://github.com/dotty-staging/scalatest/commits/dotty
4. Once your project compiles with Dotty. Open a PR against this repo that:
    - adds a test for your project in `src/test/dotty/communitybuild/CommunityBuildTest.scala`.
  A test requires:
        - Your project git url
        - The name of the branch to build. The default is `dotty`
        - The command `sbt` needs to run in order to build the project
    - adds an entry for your project in `.travis.yml` that matches the name of the test
