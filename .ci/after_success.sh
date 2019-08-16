#!/usr/bin/env bash
set -euo pipefail

if [[ "${UPDATE_SNAPSHOTS}" == "true" ]]; then
  echo "Uploading code coverage results to codecov.io"
  curl -s https://raw.githubusercontent.com/codecov/codecov-bash/master/codecov > codecov
  chmod +x codecov
  ./codecov
  .ci/update_snapshots.sh
fi

# vim :set ts=2 sw=2 sts=2 et:
