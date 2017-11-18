#!/usr/bin/env bash
set -euo pipefail

JAVA_VER=$(java -version 2>&1 | grep -i version | sed 's/.*version ".*\.\(.*\)\..*"/\1/; 1q')

echo "Checking java version: ${JAVA_VER}"
if [[ "${JAVA_VER}" == 8 ]]; then
  echo "Uploading code coverage results to codecov.io"
  curl -s https://raw.githubusercontent.com/codecov/codecov-bash/master/codecov > codecov
  chmod +x codecov
  ./codecov
  .ci/update_snapshot_docs.sh
fi

# vim :set ts=2 sw=2 sts=2 et:
