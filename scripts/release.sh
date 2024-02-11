#!/usr/bin/env bash
set -uo pipefail

function cleanup {
  EXIT_CODE=$?
  set +e # disable termination on error
  if [[ $EXIT_CODE != 0 ]]; then
    # Something went wrong...
    # originMessage=$(git log -1 HEAD --pretty=format:%s)

    # if [ ! -z ${newVersion+x} ]; then
    #   if [ "$originMessage" == "(release) TestFX $newVersion" ]; then
    #     # Roll back the commit
    #     git reset --hard HEAD^
    #   else
    #     # Didn't actually make a commit, so just reset local work-tree
    #     git reset --hard HEAD
    #   fi

    #   # Check if we pushed this commit to upstream
    #   upstreamMessage=$(git log "$upstream"/master -1 HEAD --pretty=format:%s)
    #   if [ "$upstreamMessage" == "(release) TestFX $newVersion" ]; then
    #     git push upstream master --force
    #   fi

    #   # Check if we tagged the commit
    #   if git ls-remote --tags "$upstream" | grep \""$newVersion"\"; then
    #     git push origin :"$newVersion"
    #   fi
    # fi

    # Delete the working branch
    if [ "$(git branch --show-current)" != "$oldBranch" ]; then
      git checkout "$oldBranch"
      git branch -D "$newBranch"
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

Prerequisites:
- git - Version control software (a.k.a. "the stupid content tracker")
- hub - GitHub command line tool
- ruby - Interpreted object-oriented scripting language
- github_changelog_generator - The Ruby github_changelog_generator gem

Requires a Github API key for the TestFX repository.

You can skip manually entering the API key be specifying
the \$TESTFX_GITHUB_API_KEY environment variable.

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

# Check if the user is at the root level of the project
if [[ ! $(git rev-parse --show-toplevel 2>/dev/null) = "$PWD" ]]; then
  echo "You are not currently at the root of the TestFX git repository."
  exit
fi

# Check if Git is installed
if ! [ -x "$(command -v git)" ]; then
  echo 'Error: git is not installed.' >&2
  exit 1
fi

# Check if Hub is installed
if ! [ -x "$(command -v hub)" ]; then
  echo 'Error: hub is not installed.' >&2
  exit 1
fi

# Check if Ruby is installed
if ! [ -x "$(command -v ruby)" ]; then
  echo 'Error: ruby is not installed (needed to generate changelog).' >&2
  exit 1
fi

# Check if the Ruby github_changelog_generator is installed
if [ "$(gem list -i github_changelog_generator)" != true ]; then
  echo 'Error: github_changelog_generator is not installed).' >&2
  echo 'Run \"[sudo] gem install github_changelog_generator\" to install it.' >&2
fi

# Get the upstream repository path
upstream=$(git remote -v | awk '$2 ~ /github.com[:\/]testfx\/testfx/ && $3 == "(fetch)" {print $1; exit}')
if [[ -z "$upstream" ]]; then
  echo "Could not find a git remote for the upstream TestFX repository."
  echo "See: https://github.com/TestFX/TestFX/wiki/Issuing-a-Release#local-git-repository-setup"
  exit
fi

# Find or read the users TestFX GitHub API key
if [ -z "${TESTFX_GITHUB_API_KEY}" ]; then
  echo "\"TESTFX_GITUB_API_KEY\" environment variable not set"
  read -rp "Please enter your Github API key for TestFX: " githubApiKey
else
  githubApiKey="$TESTFX_GITHUB_API_KEY"
fi

# Determine the current version
currentVersion=$(git tag --list --sort=creatordate | grep 'v*[0-9].*[0-9].*[0-9]' | tail -n1)
if [ -z "${currentVersion}" ]; then
  echo "Could not determine current version, missing tags?"
  echo "To fetch the tags run \"git fetch --tags\""
fi

echo "Current version of TestFX: $currentVersion"
IFS='.' read -ra version_parts <<< "$currentVersion"
IFS='-' read -ra classifier_parts <<< "$currentVersion"

major=${version_parts[0]:1}
minor=${version_parts[1]}
patch=${version_parts[2]%%-*}
#classifier=${classifier_parts[1]}

echo "major: $major"
echo "minor: $minor"
echo "patch: $patch"
#echo "classifier: $classifier"

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

# Determine the new project version
newVersion="v$major.$minor.$patch"
echo "The next release of TestFX will be: $newVersion"
echo "Bumping version in gradle.properties to ${newVersion:1}"

# Get the current branch
oldBranch="$(git branch --show-current)"

# Create a branch named "$newVersion-release"
newBranch="$newVersion-release"
git checkout -b "$newBranch"

# Update the project version
sed -i "/version =/ s/=.*/= ${newVersion:1}/" gradle.properties
echo "Replacing ${currentVersion:1} with ${newVersion:1} in README.md..."
sed -i -e "s/${currentVersion:1}/${newVersion:1}/g" README.md

# Generate the change log
echo "Generating changelog..."
github_changelog_generator -u testfx -p testfx --token "$githubApiKey" \
                           --output CHANGES.md --no-issues \
                           --future-release "$newVersion"

# Commit, but do not push, the project version and change log
git commit -am "(release) TestFX $newVersion"

# Push the changes to the origin
git push --set-upstream origin "$newBranch"

# Create a pull request on the upstream project
hub pull-request -o -m "$newVersion"

# The below method uses a direct push to master, keep it in case we change our mind.
if false ; then
  # Find the users GPG key that has "(TestFX)" in its' name
  gpgKey=$(gpg --list-keys --with-colon | grep '^uid' | grep '(TestFX)' | cut -d':' -f6)
  if [[ -z "$gpgKey" ]]; then
    echo "Could not find a GPG key with (TestFX) in its' name."
    echo "See: https://github.com/TestFX/TestFX/wiki/Issuing-a-Release#create-a-testfx-gpg-key"
    exit 1
  fi

  echo "Pushing tagged release commit to remote: $upstream"
  git push "$upstream" master
  git fetch "$upstream"
  git rebase "$upstream"/master

  git tag -s "$newVersion" -m \""$newVersion"\" -u "$gpgKey"
  git push upstream "$newVersion"
fi

# vim :set ts=2 sw=2 sts=2 et:
