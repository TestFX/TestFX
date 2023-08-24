#!/usr/bin/env bash

# This script was derived from instructions found at https://central.sonatype.org/publish/publish-manual/#bundle-creation

set -uo pipefail

function cleanup {
  EXIT_CODE=$?
  set +e # disable termination on error
#  if [[ $EXIT_CODE != 0 ]]; then
#    # Logic to clean up from script
#  fi
  exit $EXIT_CODE
}
trap cleanup EXIT INT TERM # Are all three of these the right choice?

# Usage info
show_help() {
cat << EOF
Usage: ${0##*/} [-h]
Helper for deploying a signed maven bundle of TestFX to Maven Central.

Prerequisites:
- gpg - PGP encryption and signing tool

Requires a Maven key for the TestFX repository.

Options:
    -h / --help       display this help and exit
EOF
}

# Show the help information
if [[ $# -gt 0 ]]; then
  if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
    show_help
    exit 0
  else
    show_help
    exit 1
  fi
fi

# Check if the user is at the root level of the project
if [[ ! $(git rev-parse --show-toplevel 2>/dev/null) = "$PWD" ]]; then
  echo "You are not currently at the root of the TestFX git repository."
  exit
fi

# Check if gpg is installed
if ! [ -x "$(command -v gpg)" ]; then
  echo 'Error: gpg is not installed.' >&2
  exit 1
fi

# Determine which gpg key to use
gpgKey=$(gpg --list-keys --with-colon | grep '^uid' | grep '(TestFX)' | cut -d':' -f10)
if [[ -z "$gpgKey" ]]; then
  echo "Could not find a GPG key with (TestFX) in its' name."
  echo "See: https://github.com/TestFX/TestFX/wiki/Issuing-a-Release#create-a-testfx-gpg-key"
  exit 1
fi

sign() {
  libsPath=subprojects/$1/build/libs

  # Remove prior files
  rm -f "$libsPath"/*.asc

  # Determine which gpg key to use
  gpgKey=$(gpg --list-keys --with-colon | grep '^uid' | grep '(TestFX)' | cut -d':' -f10)

  # Sign each file in the libs folder
  for file in $libsPath/*; do
    echo Sign $file
    gpg -ab --batch -u "$gpgKey" $file
  done
}

bundle() {
  libsPath=subprojects/$1/build/libs
  bundle="bundle.jar"

  # Remove prior files
  rm -f "$libsPath/$bundle"

  echo Create bundle
  cd $libsPath || exit
  jar -cvf bundle.jar ./*
  cd - || exit

  echo Bundle created "$libsPath/$bundle"
}

artifacts=("testfx-core" "testfx-junit" "testfx-junit5" "testfx-spock")

for artifact in "${artifacts[@]}"; do
  sign "$artifact"
done
for artifact in "${artifacts[@]}"; do
  bundle "$artifact"
done
