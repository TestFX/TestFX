# TestFX

[![Travis CI](https://img.shields.io/travis/TestFX/TestFX/master.svg?label=travis&style=flat-square)](https://travis-ci.org/TestFX/TestFX)
[![AppVeyor Build Status](https://img.shields.io/appveyor/ci/testfx/testfx/master.svg?style=flat-square)](https://ci.appveyor.com/project/testfx/testfx/branch/master)
[![Coveralls Test Coverage](https://img.shields.io/coveralls/github/TestFX/TestFX/master.svg?style=flat-square)](https://coveralls.io/github/TestFX/TestFX)
[![Bintray JCenter](https://img.shields.io/bintray/v/testfx/testfx/testfx-core.svg?label=bintray&style=flat-square)](https://bintray.com/testfx/testfx)
[![Maven Central](https://img.shields.io/maven-central/v/org.testfx/testfx-core.svg?label=maven&style=flat-square)](https://search.maven.org/#search|ga|1|org.testfx)
[![Chat on Gitter](https://img.shields.io/gitter/room/testfx/testfx-core.svg?style=flat-square)](https://gitter.im/TestFX/TestFX)

Simple and clean testing for JavaFX.

TestFX requires a minimum JDK version of 8 (1.8).

## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers and assertions to verify expected states of JavaFX scene-graph nodes.

**Support for:**

- Java 8/9/10/11
- Multiple testing frameworks ([Junit 4](http://junit.org/junit4/), [Junit 5](http://junit.org/junit5/), and [Spock](http://spockframework.org/)).
- [Hamcrest](http://hamcrest.org/) matchers or [AssertJ](http://joel-costigliola.github.io/assertj/) assertions (or both!).
- Screenshots of failed tests.
- Headless testing using [Monocle](https://github.com/TestFX/Monocle).

## Gradle

To add a dependency on TestFX using Gradle, use the following:

```gradle
dependencies {
    testCompile "org.testfx:testfx-core:4.0.14-alpha"
}
```

Next add a dependency corresponding to the testing framework you are using in
your project. TestFX supports JUnit 4, JUnit 5, and Spock. For example if
you are using JUnit 4 in your project you would add a dependency on `testfx-junit`:

```gradle
dependencies {
    testCompile "org.testfx:testfx-junit:4.0.14-alpha"
}
```

## Maven

To add a dependency on TestFX using Maven, use the following:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.14-alpha</version>
    <scope>test</scope>
</dependency>
```

Next add a dependency corresponding to the testing framework you are using in
your project. TestFX supports JUnit 4, JUnit 5, and Spock. For example if
you are using JUnit 4 in your project you would add a dependency on `testfx-junit`:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-junit</artifactId>
    <version>4.0.14-alpha</version>
    <scope>test</scope>
</dependency>
```

## Examples
### JUnit 4 with Hamcrest Matchers

```java
import org.testfx.framework.junit.ApplicationTest;

public class DesktopPaneTest extends ApplicationTest {

    DesktopPane desktopPane;

    @Override
    public void start(Stage stage) {
        desktopPane = new DesktopPane();
        desktopPane.setId("desktop");
        Scene scene = new Scene(desktopPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void should_drag_file_into_trashcan() {
        // given:
        rightClickOn("#desktop").moveTo("New").clickOn("Text Document");
        write("myTextfile.txt").push(KeyCode.ENTER);

        // when:
        drag(".file").dropTo("#trash-can");

        // then:
        verifyThat("#desktop", hasChildren(0, ".file"));
    }
}
```

### JUnit 4 with AssertJ Assertions

```java
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class DesktopPaneTest extends ApplicationTest {

    DesktopPane desktopPane;

    @Override
    public void start(Stage stage) {
        desktopPane = new DesktopPane();
        desktopPane.setId("desktop");
        Scene scene = new Scene(desktopPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void should_drag_file_into_trashcan() {
        // given:
        rightClickOn("#desktop").moveTo("New").clickOn("Text Document");
        write("myTextfile.txt").push(KeyCode.ENTER);

        // when:
        drag(".file").dropTo("#trash-can");

        // then:
        assertThat(desktopPane).hasChildren(0, ".file");
        // or (lookup by css id):
        assertThat(lookup("#desktop").queryAs(DesktopPane.class)).hasChildren(0, ".file");
        // or (look up css class):
        assertThat(lookup(".desktop-pane").queryAs(DesktopPane.class)).hasChildren(0, ".file");
    }
}
```

### JUnit 5 with Hamcrest Matchers

```java
import org.testfx.framework.junit5.ApplicationTest;

@ExtendWith(ApplicationExtension.class)
class ClickableButtonTest {

    @Start
    void onStart(Stage stage) {
        Button button = new Button("click me!");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    void should_contain_button() {
        // expect:
        verifyThat(".button", hasText("click me!"));
    }

    @Test
    void should_click_on_button(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // then:
        verifyThat(".button", hasText("clicked!"));
    }
}
```

### JUnit 5 with AssertJ Assertions

```java
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class ClickableButtonTest {

    Button button;

    @Start
    void onStart(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    void should_contain_button() {
        // expect:
        assertThat(button).hasText("click me!");
        // or (lookup by css id):
        assertThat(lookup("#myButton").queryAs(Button.class)).hasText("click me!");
        // or (lookup by css class):
        assertThat(lookup(".button").queryAs(Button.class)).hasText("click me!");
        // or (query specific type):
        assertThat(lookup(".button").queryButton()).hasText("click me!");
    }

    @Test
    void should_click_on_button(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // then:
        assertThat(button).hasText("clicked!");
        // or (lookup by css id):
        assertThat(lookup("#myButton").queryAs(Button.class)).hasText("clicked!");
        // or (lookup by css class):
        assertThat(lookup(".button").queryAs(Button.class)).hasText("clicked!");
        // or (query specific type)
        assertThat(lookup(".button").queryButton()).hasText("clicked!");
    }
}
```

### Spock with Hamcrest Matchers

```java
import org.testfx.framework.spock.ApplicationSpec;

class ClickableButtonSpec extends ApplicationSpec {
    @Override
    void init() throws Exception {
        FxToolkit.registerStage { new Stage() }
    }

     @Override
    void start(Stage stage) {
        Button button = new Button('click me!')
        button.setOnAction { button.setText('clicked!') }
        stage.setScene(new Scene(new StackPane(button), 100, 100))
        stage.show()
    }

    @Override
    void stop() throws Exception {
        FxToolkit.hideStage()
    }

    def "should contain button"() {
        expect:
        verifyThat('.button', hasText('click me!'))
    }

    def "should click on button"() {
        when:
        clickOn(".button")

        then:
        verifyThat('.button', hasText('clicked!'))
    }
}
```

## Continuous Integration (CI)

### Travis CI

To run TestFX tests as part of your Travis CI build on Ubuntu and/or macOS
take the following steps:

1. Ensure that your unit tests are triggered as part of your build script. This
   is usually the default case when using Maven or Gradle.
2. If you wish to test in a headless environment your must add [Monocle](https://github.com/TestFX/Monocle)
    as a test dependency:

`build.gradle`
```gradle
dependencies {
    testCompile "org.testfx:openjfx-monocle:8u76-b04" // jdk-9+181 for Java 9
}
```

`pom.xml`
```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>openjfx-monocle</artifactId>
    <version>8u76-b04</version> <!-- jdk-9+181 for Java 9 -->
    <scope>test</scope>
</dependency>
```
3. Base your Travis configuration on the following. Some different build variations are shown (Glass/AWT robot,
    Headed/Headless, (Hi)DPI, etc.) adjust the build matrix to your requirements.

`.travis.yml`
``` yaml
language: java

sudo: false   # Linux OS: run in container

matrix:
  include:
    # Ubuntu Linux (trusty) / Oracle JDK 8 / Headed (AWT Robot)
    - os: linux
      dist: trusty
      jdk: oraclejdk8
      env:
        - _JAVA_OPTIONS="-Dtestfx.robot=awt"
    # Ubuntu Linux (trusty) / Oracle JDK 8 / Headed (Glass Robot) / HiDPI
    - os: linux
      dist: trusty
      jdk: oraclejdk8
      env:
        - _JAVA_OPTIONS="-Dtestfx.robot=glass -Dglass.gtk.uiScale=2.0"
    # Ubuntu Linux (trusty) / Oracle JDK 8 / Headless
    - os: linux
      dist: trusty
      jdk: oraclejdk8
      env:
        - _JAVA_OPTIONS="-Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw"
    # macOS / Oracle JDK 8 / Headless
    - os: osx
      osx_image: xcode9.4
      jdk: oraclejdk8
      env:
        - _JAVA_OPTIONS="-Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw -Dprism.verbose=true"
    # Headed macOS is not currently possible on Travis.

addons:
  apt:
    packages:
      - oracle-java8-installer

before_install:
  - if [[ "${TRAVIS_OS_NAME}" == linux ]]; then export DISPLAY=:99.0; sh -e /etc/init.d/xvfb start; fi

install: true

before_script:
  - if [[ "${TRAVIS_OS_NAME}" == osx ]]; then brew update; brew cask reinstall caskroom/versions/java8; fi

script:
  - ./gradlew check

before_cache:
  - rm -f  $HOME/.gradle/caches/modules-2/modules-2.lock
  - rm -fr $HOME/.gradle/caches/*/plugin-resolution/
  - rm -f  $HOME/.gradle/caches/*/fileHashes/fileHashes.bin
  - rm -f  $HOME/.gradle/caches/*/fileHashes/fileHashes.lock

cache:
  directories:
    - $HOME/.gradle/caches/
    - $HOME/.gradle/wrapper/
```

Your TestFX tests should now run as part of your Travis CI build.

### Appveyor (Windows)

To run TestFX tests as part of your Appveyor build on Windows take the following
steps:

1. Ensure that your unit tests are triggered as part of your build script. This
   is usually the default case when using Maven or Gradle.
2. If you wish to test in a headless environment your must add [Monocle](https://github.com/TestFX/Monocle)
    as a test dependency:

`build.gradle`
```gradle
dependencies {
    testCompile "org.testfx:openjfx-monocle:8u76-b04" // jdk-9+181 for Java 9
}
```

`pom.xml`
```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>openjfx-monocle</artifactId>
    <version>8u76-b04</version> <!-- jdk-9+181 for Java 9 -->
    <scope>test</scope>
</dependency>
```
3. Base your Appveyor configuration on the following. Some different build variations are shown (Glass/AWT robot,
    Headed/Headless, (Hi)DPI, etc.) adjust the build matrix to your requirements.

`appveyor.yml`
```yaml
version: "{branch} {build}"
environment:
  matrix:
    # Java 8 / AWT Robot
    - JAVA_VERSION: "8"
      JAVA_HOME: C:\Program Files\Java\jdk1.8.0
      _JAVA_OPTIONS: "-Dtestfx.robot=awt -Dtestfx.awt.scale=true"
    # Java 8 / AWT Robot / HiDPI
    - JAVA_VERSION: "8"
      JAVA_HOME: C:\Program Files\Java\jdk1.8.0
      _JAVA_OPTIONS: "-Dtestfx.robot=awt -Dtestfx.awt.scale=true -Dglass.win.uiScale=200%"
    # Java 8 / Headless
    - JAVA_VERSION: "8"
      JAVA_HOME: C:\Program Files\Java\jdk1.8.0
      _JAVA_OPTIONS: "-Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw -Dprism.text=t2k"
    # Java 10 / AWT Robot / HiDPI
    - JAVA_VERSION: "10"
      JAVA_HOME: C:\jdk10
      _JAVA_OPTIONS: "-Dtestfx.robot=awt -Dtestfx.awt.scale=true -Dglass.win.uiScale=200%"
    # Java 11 / AWT Robot / HiDPI
    - JAVA_VERSION: "11"
      JAVA_HOME: C:\jdk11
      _JAVA_OPTIONS: "-Dtestfx.robot=awt -Dtestfx.awt.scale=true -Dglass.win.uiScale=200%"

build_script:
  - ps: |
      if ($env:JAVA_VERSION -eq "11") {
        $client = New-Object net.webclient
        $client.DownloadFile('http://jdk.java.net/11/', 'C:\Users\appveyor\openjdk11.html')
        $openJdk11 = cat C:\Users\appveyor\openjdk11.html | where { $_ -match "href.*https://download.java.net.*jdk11.*windows-x64.*zip\`"" } | %{ $_ -replace "^.*https:", "https:" } | %{ $_ -replace ".zip\`".*$", ".zip" }
        echo "Download boot JDK from: $openJdk11"
        $client.DownloadFile($openJdk11, 'C:\Users\appveyor\openjdk11.zip')
        Expand-Archive -Path 'C:\Users\appveyor\openjdk11.zip' -DestinationPath 'C:\Users\appveyor\openjdk11'
        Copy-Item -Path 'C:\Users\appveyor\openjdk11\*\' -Destination 'C:\jdk11' -Recurse -Force
      }
      elseif ($env:JAVA_VERSION -eq "10") {
        choco install jdk10 --version 10.0.2 --force --cache 'C:\ProgramData\chocolatey\cache' -params 'installdir=c:\\jdk10'
      }

      // Note: Currently Java 8 is the default JDK, if that changes the above will have to change accordingly.

shallow_clone: true

build:
  verbosity: detailed

test_script:
  - gradlew build --no-daemon

cache:
  - C:\Users\appveyor\.gradle\caches
  - C:\Users\appveyor\.gradle\wrapper -> .gradle-wrapper\gradle-wrapper.properties
  - C:\ProgramData\chocolatey\bin -> appveyor.yml
  - C:\ProgramData\chocolatey\lib -> appveyor.yml
  - C:\ProgramData\chocolatey\cache -> appveyor.yml
```

## Documentation

* [Javadocs](http://testfx.github.io/TestFX/docs/javadoc/) for latest `master`
* The [wiki](https://github.com/TestFX/TestFX/wiki)
* The changelog [CHANGES.md](https://github.com/TestFX/TestFX/blob/master/CHANGES.md)

## Chat

Head over to our [gitter chat](https://gitter.im/TestFX/TestFX) for discussion and questions.

## TestFX Legacy: Deprecated

The `testfx-legacy` subproject is deprecated and no longer supported. It is highly recommended
that you switch from using `testfx-legacy`. If you want to continue using it you should cap
the versions of `testfx-core` and `testfx-legacy` to `4.0.8-alpha`, which was the last released
version of `testfx-legacy`. Using a newer version of `testfx-core` with an older version of
`testfx-legacy` will very likely break (and does with `testfx-core` versions past `4.0.10-alpha`).

## Credits

Thanks to all of the [contributors of TestFX](https://github.com/TestFX/TestFX/graphs/contributors)!
