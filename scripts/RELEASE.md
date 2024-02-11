# TestFX Release Steps

## Prerequisites

The project should be successfully building in GitHub Actions on the `master` 
branch before proceeding with a release. Once all pull requests for the 
release have been merged and the `master` branch is building successfully, you 
are ready to make a release.

The release script will clean up failed runs each time it starts, so be 
confident that you can run the script as many times as needed to be successful.

## Steps
1. On your local machine, navigate to the root folder of the project
1. Ensure that you are on the `master` branch: `git checkout master`
1. Ensure that you have the latest changes from source control: `git pull`
1. Run the `release.sh` script: `./scripts/release.sh`
1. Fix any errors that the script finds and run the script again
1. When asked if this is a major, minor or patch release
    1. Review the current version information for accuracy
    1. Choose wisely what version part you want to increment
1. Once you choose what version part to increment the script will do the following
    1. Generate a change log
    1. Create a new branch with all the changes for the release
    1. Push the new branch to GitHub and creates a pull request
    1. Opens a browser to the pull request for review
1. Once the browser window is open, the script is finished

## Completing the Release

Request that the PR be reviewed and merge by a repository maintainer. Once the PR is merged, the release is complete and new libraries can be build based on the new repository tag.
