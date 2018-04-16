#!/usr/bin/env bash
set -euo pipefail

if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
  brew update
  if [[ "${TRAVIS_JAVA_VERSION}" == 8 ]]; then
    brew cask reinstall caskroom/versions/java8
  elif [[ "${TRAVIS_JAVA_VERSION}" == 9 ]]; then
    brew cask reinstall caskroom/versions/java9
  elif [[ "${TRAVIS_JAVA_VERSION}" == 10 ]]; then
    brew cask install java
  else
    echo "TRAVIS_JAVA_VERSION environment variable not set!"
    exit 1
  fi
  brew unlink python # fixes 'run_one_line' is not defined error in backtrace
fi

if [[ "${TRAVIS_OS_NAME}" == linux ]]; then
  wget https://raw.githubusercontent.com/sormuras/bach/master/install-jdk.sh
fi

chmod +x .ci/update_snapshot_docs.sh

# vim :set ts=2 sw=2 sts=2 et:
