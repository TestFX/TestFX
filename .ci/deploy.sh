#!/usr/bin/env bash
set -euo pipefail

# Install latest Java 8 and Java 9
brew update
brew cask reinstall caskroom/versions/java8
brew cask reinstall java

# Upload release artifacts to Bintray
./gradlew bintray -PbintrayUsername="$BINTRAY_USERNAME" -PbintrayApiKey="$BINTRAY_API_KEY" -Dorg.gradle.java.home="$(/usr/libexec/java_home -v 8)"

# Sync bintray artifacts with Maven Central
version=$(git tag -l --points-at HEAD) # Replace with $TRAVIS_TAG?
declare -a subprojects=("testfx-core" "testfx-junit" "testfx-junit5" "testfx-spock")
for subproject in "${subprojects[@]}"
do
  curl -u "$BINTRAY_USERNAME":"$BINTRAY_API_KEY" \
       -H "Content-Type: application/json" \
       -X POST \
       -d "{\"username\":\"$SONATYPE_USERNAME\",\"password\":\"$SONATYPE_PASSWORD\",\"close\":\"1\"}" \
       https://bintray.com/api/v1/central_sync/testfx/testfx/"$subproject"/versions/"${version:1}"
done

# vim :set ts=2 sw=2 sts=2 et:
