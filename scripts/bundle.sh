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

# Check if Git is installed
if ! [ -x "$(command -v gpg)" ]; then
  echo 'Error: gpg is not installed.' >&2
  exit 1
fi

bundle() {
  echo Bundle $1
  libsPath=subprojects/$1/build/libs
  bundle="$libsPath/bundle.jar"

  # Remove prior files
  rm -f "$libsPath"/*.asc
  rm -f "$bundle"

  # Determine which gpg key to use
  gpgKey=$(gpg --list-keys --with-colon | grep '^uid' | grep '(TestFX)' | cut -d':' -f10)

  # Sign each file in the libs folder
  for file in $libsPath/*; do
    echo Sign $file
    gpg -ab --batch -u "$gpgKey" $file
  done

  echo Create bundle
  cd $libsPath || exit

  # Collect the bundle files
  bundleFiles=""
  for file in *; do
    bundleFiles="$bundleFiles $file"
  done

  # Create the bundle
  jar -cvf bundle.jar $bundleFiles

  cd - || exit

  echo Bundle created $bundle
}

deploy() {
  echo Deploy $1
  libsPath=subprojects/$1/build/libs
  bundle="$libsPath/bundle.jar"
}

bundle testfx-core
#bundle testfx-junit
#bundle testfx-junit5
#bundle testfx-spock

#deploy testfx-core
#deploy testfx-junit
#deploy testfx-junit5
#deploy testfx-spock
