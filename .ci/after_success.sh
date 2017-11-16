#!/usr/bin/env bash
set -euo pipefail

JAVA_VER=$(java -version 2>&1 | grep -i version | sed 's/.*version ".*\.\(.*\)\..*"/\1/; 1q')

echo "Checking java version: ${JAVA_VER}"
if [[ "${JAVA_VER}" == 8 ]]; then
  echo "Uploading code coverage results to codecov.io"
  # Download codecov bash from master branch as codecov.io/bash lags behind
  curl -s https://raw.githubusercontent.com/codecov/codecov-bash/master/codecov > codecov
  chmod +x codecov
  script=$(cat ./codecov.yml)
  export projects="testfx-core testfx-junit testfx-junit5 testfx-spock"
  for project in ${projects};
  do
    # Add directory fixes for each subproject so source code navigation on codecov.io works correctly.
    printf "%s\nfixes:\n  - \"::subprojects/%s/\"\n" "$script" "$project" >> ./codecov_"$project".yml
    echo "------- Code Coverage File: --------"
    cat ./codecov_"$project".yml
    echo "------- End Code Coverage File --------"
    ./codecov -f "subprojects/$project/build/reports/jacoco/test/jacocoTestReport.xml" -F ${project//-/_} -y codecov_"$project".yml
  done
  .ci/update_snapshot_docs.sh
fi

# vim :set ts=2 sw=2 sts=2 et:
