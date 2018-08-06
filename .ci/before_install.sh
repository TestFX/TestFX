#!/usr/bin/env bash
set -euo pipefail

if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
  brew update
  brew unlink python # fixes 'run_one_line' is not defined error in backtrace
fi

chmod +x .ci/update_snapshot_docs.sh

# vim :set ts=2 sw=2 sts=2 et:
