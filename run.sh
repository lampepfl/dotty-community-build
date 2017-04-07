#!/usr/bin/env bash
set -eux
git submodule update --recursive --remote
cd dotty && sbt publishLocal && cd ..
cd dotty-community-build && sbt test
