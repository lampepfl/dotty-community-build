# Dotty Community Build
[![Community Build Status](https://travis-ci.org/lampepfl/dotty-community-build.svg?branch=master)](https://travis-ci.org/lampepfl/dotty-community-build)

This repository contains tests to build a corpus of Scala open sources projects
against the latest changes in Dotty.

To run the community build on a local machine, clone the repo and execute `./run.sh`.

The tests will by default run against the latest NIGHTLY build of dotty.
To customize the dotty version, set the environment variable `export DOTTY_VERSION=X.Y.Z`.

## Adding a new project
To add a new project `ORG/REPO` to the community build you can follow this steps:
1. Fork `ORG/REPO` to `dotty-staging/REPO`
2. Clone `dotty-staging/REPO`, create a branch `dotty` and push the `dotty` branch 
3. Open `dotty-staging/REPO` on Github. Go to `Settings > Branches`. Change the default branch to `dotty`
4. Go to https://travis-ci.org/profile/dotty-staging and enable that repo. You may need to click on `Sync account` and
   reload to see the newly forked repo
5. Fork the `dotty-staging/REPO` to your personal github account
6. Edit the build to use `sbt-dotty` and fix all the compilation errors. Example PRs:
    - https://github.com/dotty-staging/scalacheck/pull/1
    - https://github.com/dotty-staging/squants/pull/1
    - https://github.com/dotty-staging/algebra/pull/1
7. Once the project complies with dotty, copy the `.travis.yml` file from
   [here](https://github.com/dotty-staging/squants/blob/37ea54761b5e80ddc0dfc273fc4bbad430f201f5/.travis.yml#L3)
   and adjust the compile task. It's OK to only compile the project to begin with, not run any tests.
8. Open PR to merge your changes into the `dotty` branch of hte `dotty-staging/REPO`
9. Once PR is merged into `dotty-staging/REPO`, open a PR to `lampepfl/dotty-community-build` adding the new
   `dotty-staging/REPO` to the community build. Example PR: https://github.com/lampepfl/dotty-community-build/pull/3
