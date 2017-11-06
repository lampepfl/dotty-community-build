#!/usr/bin/env bash
set -eux
set -o pipefail

RESET="\033[0m"
GREEN="\033[32m"

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" >& /dev/null && pwd)"
BUILD_DIR="$(mktemp -d)"
REFERENCE=${DOTTY_REFERENCE:-}
DOTTY_VERSION=""

if [ ! -z $REFERENCE ]; then
    echo -e "${GREEN}Building Dotty...${RESET}"
    pushd "$BUILD_DIR"
    git clone https://github.com/lampepfl/dotty.git .
    git fetch origin "$REFERENCE"
    git checkout -qf FETCH_HEAD
    git submodule update --init --recursive
    DOTTY_VERSION=$(sbt -no-colors "dotty-bootstrapped/publishLocal" "show dotty-bootstrapped/version" \
                      | tail -1 \
                      | cut -d' ' -f2 \
                      | tr -d '[:space:]')
    popd

    rm -rf "$BUILD_DIR"
    export DOTTY_VERSION
fi

sbt test
