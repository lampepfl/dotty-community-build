#!/usr/bin/env bash
set -eux
git submodule update --recursive --remote
cd dotty-community-build && sbt test
