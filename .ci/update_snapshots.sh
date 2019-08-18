#!/usr/bin/env bash
set -euo pipefail

# see http://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci/ for details

function prop {
    grep "${1}" "${TRAVIS_BUILD_DIR}"/gradle.properties | cut -d'=' -f2
}

if [ "$TRAVIS_REPO_SLUG" == "TestFX/TestFX" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] && \
   [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Publishing Javadoc..."

  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"

  cd "$HOME"
  echo "Cloning gh-pages branch"
  git clone --branch=gh-pages https://"${GH_TOKEN}"@github.com/TestFX/TestFX gh-pages

  export projects="testfx-core testfx-junit testfx-junit5 testfx-spock"
  for project in ${projects};
  do
    doctype="javadoc"
    if [ "$project" = "testfx-spock" ]; then
      doctype="groovydoc"
    fi
    mkdir -p "$HOME"/javadoc/"$project"
    cp -R "$HOME"/build/TestFX/TestFX/subprojects/"$project"/build/docs/"$doctype" "$HOME"/javadoc/"$project"
    cd "$HOME"/gh-pages
    git rm -rf --ignore-unmatch ./docs/javadoc/"$project"
    cp -Rf "$HOME"/javadoc/"$project" "$HOME"/gh-pages/docs/javadoc
  done
    cd "$HOME"/gh-pages
    echo "$PWD"
    git add -f .
    git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages."
    git push -fq origin gh-pages
    echo "Published Javadoc to gh-pages."

    if [[ $(prop 'version') = *-SNAPSHOT ]]; then
      echo "Publishing snapshot to Sonatype snapshot repository..."
      cd "${TRAVIS_BUILD_DIR}"
      ./gradlew publishMavenJavaPublicationToMavenRepository -P sonatypeUsername="$SONATYPE_USERNAME" -P sonatypePassword="$SONATYPE_PASSWORD"
    fi
fi

# vim :set ts=2 sw=2 sts=2 et:
