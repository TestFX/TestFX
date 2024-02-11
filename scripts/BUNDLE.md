# TestFX Release Steps

## Prerequisites

The project should already have been successfully released and the release PR 
merged into the `master` branch before building library bundles for Maven.

You will need your GPG Key passphrase for signing the library bundle files. It 
is suggested to have it on the clipboard before running the bundle script.

## Steps
1. On your local machine, navigate to the root folder of the project
1. Ensure that you are using the tag for the release you want to build libraries for: `git checkout v4.0.18`
1. Build the project libraries: `./gradlew clean build -x test`
1. Run the `bundle.sh` script: `./scripts/bundle.sh`
1. Fix any errors that the script finds and run the script again
1. When prompted for your GPG key passphrase, paste it from the clipboard
1. The script will do the following
    1. Sign all the libraries with your GPG key
    1. Create bundle file in `build/bundles`
1. Once the bundle files are created, the script is finished

## Deployment
General instructions can be found here: https://central.sonatype.org/publish/publish-manual/

1. Navigate to https://oss.sonatype.org/ and log in
1. Select Staging Upload in the left side menu
1. For each bundle:
    1. Change Upload Mode to `Artifact Bundle`
    1. Click the `Select Bundle to Upload` button
    1. Navigate to the bundle file on your local system and select it
    1. Click the `Upload Bundle` button
    1. The bundle file will be uploaded and a Staging Repository created
1. Once all the artifact bundles have been uploaded review the Staging Repositories
1. Ask a repository maintainer to review the Staging Repositories
1. Once reviewed, release them
