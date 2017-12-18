#!/usr/bin/env bash
set -euo pipefail

brew update
brew cask reinstall caskroom/versions/java8
brew cask reinstall java
./gradlew bintray -PbintrayUsername=$BINTRAY_USERNAME -PbintrayApiKey=$BINTRAY_API_KEY -Dorg.gradle.java.home="$(/usr/libexec/java_home -v 8)"
./gradlew :testfx-internal-java9:bintray -PbintrayUsername=$BINTRAY_USERNAME -PbintrayApiKey=$BINTRAY_API_KEY -Dorg.gradle.java.home="$(/usr/libexec/java_home -v 9)"
