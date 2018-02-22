#!/usr/bin/env bash
set -euo pipefail

function cleanup {
  EXIT_CODE=$?
  set +e # disable termination on error
  if [[ $EXIT_CODE != 0 ]]; then
    # Something went wrong...
    originMessage=$(git log -1 HEAD --pretty=format:%s)
    if [ ! -z ${newVersion+x} ]; then
      if [ "$originMessage" == "(release) TestFX $newVersion" ]; then
        # Roll back the commit
        git reset --hard HEAD^
      else
        # Didn't actually make a commit, so just reset local work-tree
        git reset --hard HEAD
      fi

      # Check if we pushed this commit to upstream
      upstreamMessage=$(git log "$upstream"/master -1 HEAD --pretty=format:%s)
      if [ "$upstreamMessage" == "(release) TestFX $newVersion" ]; then
        git push upstream master --force
      fi

      # Check if we tagged the commit
      if git ls-remote --tags "$upstream" | grep \""$newVersion"\"; then
        git push origin :"$newVersion"
      fi
    fi
  fi
  exit $EXIT_CODE
}
trap cleanup EXIT INT TERM # Are all three of these the right choice?

# Usage info
show_help() {
cat << EOF
Usage: ${0##*/} [-h]
Helper for issuing a new release of TestFX.

Requires a Github API key for the TestFX repository.

You can skip manually entering the API key be specifying
the $TESTFX_GITHUB_API_KEY environment variable.

Options:
    -h / --help       display this help and exit
EOF
}

if [[ $# -gt 0 ]]; then
  if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
    show_help
    exit 0
  else
    show_help
    exit 1
  fi
fi

if ! [ -x "$(command -v git)" ]; then
  echo 'Error: git is not installed.' >&2
  exit 1
fi

if ! [ -x "$(command -v ruby)" ]; then
  echo 'Error: ruby is not installed (needed to generate changelog).' >&2
  exit 1
fi

if [ "$(gem list -i github_changelog_generator)" != true ]; then
  echo 'Error: github_changelog_generator is not installed).' >&2
  echo 'Run \"[sudo] gem install github_changelog_generator\" to install it.' >&2
fi

if [[ ! $(git rev-parse --show-toplevel 2>/dev/null) = "$PWD" ]]; then
  echo "You are not currently at the root of the TestFX git repository."
  exit
fi

if [ -z "${TESTFX_GITHUB_API_KEY-}" ]; then
  echo "\"TESTFX_GITUB_API_KEY\" environment variable not set"
  read -rp "Please enter your Github API key for TestFX: " githubApiKey
else
  githubApiKey="$TESTFX_GITHUB_API_KEY"
fi

currentVersion=$(git tag --list --sort=taggerdate | grep 'v*[0-9].*[0-9].*[0-9]' | tail -n1)

echo "Current version of TestFX: $currentVersion"
IFS='.' read -ra version_parts <<< "$currentVersion"
IFS='-' read -ra classifier_parts <<< "$currentVersion"

major=${version_parts[0]:1}
minor=${version_parts[1]}
patch=${version_parts[2]%%-*}
classifier=${classifier_parts[1]}

echo "major: $major"
echo "minor: $minor"
echo "patch: $patch"
echo "classifier: $classifier"

echo "Would you like to bump the major, minor, or patch version?"
echo "In x.y.z x = major, y = minor, z = patch"
echo "Enter the number corresponding to the part you want to increment:"
options=("Major" "Minor" "Patch");

select bumpType in "${options[@]}"; do
  case $bumpType in
    "Major") major=$((major + 1)); break;;
    "Minor") minor=$((minor + 1)); break;;
    "Patch") patch=$((patch + 1)); break;;
    *)       echo "Invalid option"; break;;
  esac
done

newVersion="v$major.$minor.$patch-$classifier"
echo "The next release of TestFX will be: $newVersion"
echo "Bumping versions in README.md and gradle.properties..."
sed -i "/version =/ s/=.*/= ${newVersion:1}/" gradle.properties
sed -i -e "s/${currentVersion:1}/${newVersion:1}/g" README.md
echo "Generating changelog..."
github_changelog_generator testfx/testfx --token "$githubApiKey" \
                           --output CHANGES.md --no-issues \
                           --future-release "$newVersion"
git add .
git commit -m "(release) TestFX $newVersion"
upstream=$(git remote -v | awk '$2 ~ /github.com[:\/]testfx\/testfx/ && $3 == "(fetch)" {print $1; exit}')
if [[ -z "$upstream" ]]; then
  echo "Could not find a git remote for the upstream TestFX repository."
  echo "See: https://github.com/TestFX/TestFX/wiki/Issuing-a-Release#local-git-repository-setup"
  exit 1
fi
echo "Pushing tagged release commit to remote: $upstream"
git push "$upstream" master
git fetch "$upstream"
git rebase "$upstream"/master

# Find GPG key that has "(TestFX)" in its' name
gpgKey=$(gpg --list-keys --with-colon | grep '^pub' | grep '(TestFX)' | cut -d':' -f5)
if [[ -z "$gpgKey" ]]; then
  echo "Could not find a GPG key with (TestFX) in its' name."
  echo "See: https://github.com/TestFX/TestFX/wiki/Issuing-a-Release#create-a-testfx-gpg-key"
  exit 1
fi
git tag -s "$newVersion" -m \""$newVersion"\" -u "$gpgKey"
git push upstream "$newVersion"

# The below method uses a pull request, keep it in case we change our mind.
if false ; then
  git push origin "$newVersion"-release

  installedHub=false
  if ! [ -x "$(command -v hub)" ]; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
      echo "Installing hub (command-line Github tool) via Homebrew"
      brew install hub
      installedHub=true
    elif [[ "$OSTYPE" == "linux-gnu" ]]; then
      if ! [ -x "$(command -v dnf)" ]; then
        echo "Installing hub (command-line Github tool) via dnf"
        sudo dnf install hub
        installedHub=true
      fi
    fi

    if [ "$installedHub" = false ] ; then
      echo "Downloading hub (command-line Github tool)"
      wget --quiet --output-document=hub.tgz https://github.com/github/hub/releases/download/v2.3.0-pre10/hub-linux-amd64-2.3.0-pre10.tgz
      if [[ $(sha256sum hub.tgz | head -c 64) != "015297eb81e8fe11f3989d8f65c213111e508cecf0e9de8af1b7741de2077320" ]]; then
        echo "Error (integrity): hub release download had bad checksum: $(sha256sum hub.tgz | head -c 64)" >&2
        exit
      fi
      mkdir hub-dir
      tar -xf hub.tgz -C hub-dir --strip-components 1
      rm hub.tgz
      mkdir -p .sync
      cp ./hub-dir/bin/hub .sync/
      rm -r hub-dir
      hub='./.sync/hub'
    fi
  fi

  hub='hub'
  if [ "$installedHub" = false ] ; then
    hub='./.sync/hub'
  fi

  "${hub}" pull-request -o -m "$newVersion" -b "$upstream:master"
fi

# vim :set ts=2 sw=2 sts=2 et:
