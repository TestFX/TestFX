#!/usr/bin/env bash
set -uo pipefail

ulimit -c unlimited -S

./gradlew versions build jacocoRootReport coveralls

# Print core dumps when JVM crashes.
RESULT=$?
if [[ ${RESULT} -ne 0 ]]; then
  JVMCRASH="$(find . -name "hs_err_pid*.log" -type f -print | head -n 1)"
  if [ -f "$JVMCRASH" ]; then
    echo "======= JVM Crash Log $JVMCRASH ======="
    cat "$JVMCRASH"
  fi

  CORES=''
  if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
    CORES="$(find /cores/ -type f -print)"
  else
    CORES="$(find . -type f -name 'core.*' -print)"
  fi

  if [ -n "${CORES}" ]; then
    for core in ${CORES}; do
    echo "======= Core file $core ======="
    if [[ "${TRAVIS_OS_NAME}" == osx ]]; then
      lldb --batch -o "thread backtrace all" -f "$(which java)" -c "${core}"
    else
      gdb -n -batch -ex "thread apply all bt" "$(which java)" -c "${core}"
    fi
  done
  fi

  exit ${RESULT}
fi

# vim :set ts=2 sw=2 sts=2 et:
