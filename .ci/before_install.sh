#!/usr/bin/env bash

# vim :set ts=2 sw=2 sts=2 et:
if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
  brew update
  brew install jenv
  jenv versions
  if [[ "${TRAVIS_JAVA_VERSION}" == 8 ]]; then
    brew cask reinstall java8
  elif [[ "${TRAVIS_JAVA_VERSION}" == 9 ]]; then
    brew cask reinstall java
  else
      echo "TRAVIS_JAVA_VERSION environment variable not set!"
  fi

  brew outdated gradle || brew upgrade gradle
  brew unlink python # fixes 'run_one_line' is not defined error in backtrace
fi

