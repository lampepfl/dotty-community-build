# Dotty Community Build
[![Community Build Status](https://travis-ci.org/lampepfl/dotty-community-build.svg?branch=master)](https://travis-ci.org/lampepfl/dotty-community-build)


This repository contains tests to build a corpus of Scala open sources projects
against the latest changes in Dotty.

To run the community build on a local machine, clone the repo and execute `./run.sh`.

The tests will by default run against the latest NIGHTLY build of dotty.
To customize the dotty version, set the environment variable `export DOTTY_VERSION=X.Y.Z`.
