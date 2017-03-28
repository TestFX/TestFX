#!/usr/bin/env bash

# vim :set ts=2 sw=2 sts=2 et:
if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
  brew update
  brew outdated java || brew cask install java
  brew install gradle
  brew unlink python # fixes 'run_one_line' is not defined error in backtrace
fi

