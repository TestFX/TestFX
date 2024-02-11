# TestFX 4

[![TestFX 4 CI](https://github.com/TestFX/TestFX/actions/workflows/entry.yml/badge.svg)](https://github.com/TestFX/TestFX/actions/workflows/entry.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.testfx/testfx-core.svg?label=maven&style=flat-square)](https://search.maven.org/#search|ga|1|org.testfx)
[![Chat on Gitter](https://img.shields.io/gitter/room/testfx/testfx-core.svg?style=flat-square)](https://gitter.im/TestFX/TestFX)

Simple and clean testing for JavaFX.

TestFX 4 requires Java version of 8 (1.8), or higher, and has only legacy support.

## Documentation

* See the [Javadocs](https://testfx.github.io/TestFX/docs/javadoc/) for latest `master`.
* See the changelog [CHANGES.md](https://github.com/TestFX/TestFX/blob/master/CHANGES.md) for latest released version.

## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers and assertions to verify expected states of JavaFX scene-graph nodes.

**Support for:**

- Java 8/11/17+
- Multiple testing frameworks ([JUnit 4](https://junit.org/junit4/), [JUnit 5](https://junit.org/junit5/), and [Spock](http://spockframework.org/)).
- [Hamcrest](http://hamcrest.org/) matchers or [AssertJ](https://assertj.github.io/doc/) assertions (or both!).
- Screenshots of failed tests.
- Headless testing using [Monocle](https://github.com/TestFX/Monocle).

## Gradle

To add a dependency on TestFX using Gradle, use the following:

```gradle
dependencies {
    testCompile "org.testfx:testfx-core:4.0.18"
}
```

### Java 11+
Beginning with Java 11, JavaFX is no longer part of the JDK. It has been extracted to its own project: [OpenJFX](https://openjfx.io). This means, extra dependencies must be added to your project.

The easiest way to add the JavaFX libraries to your Gradle project is to use the [JavaFX Gradle Plugin](https://github.com/openjfx/javafx-gradle-plugin).

After following the README for the JavaFX Gradle Plugin you will end up with something like:

```gradle
plugins {
    id 'org.openjfx.javafxplugin' version '0.0.8'
}

javafx {
    version = '12'
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}
```

### Test Framework
Next add a dependency corresponding to the testing framework you are using in your project. TestFX currently supports JUnit 4, JUnit 5, and Spock.

#### JUnit 4

```gradle
dependencies {
    testCompile "junit:junit:4.13-beta-3"
    testCompile "org.testfx:testfx-junit:4.0.18"
}
```

#### JUnit 5

```gradle
dependencies {
    testCompile 'org.junit.jupiter:junit-jupiter-api:5.5.1'
    testCompile "org.testfx:testfx-junit5:4.0.18"
}
```

#### Spock

```gradle
dependencies {
    testCompile "org.spockframework:spock-core:1.3-groovy-2.5"
    testCompile "org.testfx:testfx-spock:4.0.18"
}
```
### Matcher/Assertions Library
Finally you must add a dependency corresponding to the matcher/assertions libraries that you want to use with TestFX. TestFX currently supports Hamcrest matchers or AssertJ assertions.

#### Hamcrest
```gradle
testCompile group: 'org.hamcrest', name: 'hamcrest', version: '2.1'
```

#### AssertJ
```gradle
testCompile group: 'org.assertj', name: 'assertj-core', version: '3.13.2'
```

## Maven

To add a dependency on TestFX using Maven, use the following:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.18</version>
    <scope>test</scope>
</dependency>
```

### Java 11+
Beginning with Java 11, JavaFX is no longer part of the JDK. It has been extracted to its own project: [OpenJFX](https://openjfx.io). This means, extra dependencies must be added to your project.

The easiest way to add the JavaFX libraries to your Maven project is to use the [JavaFX Maven Plugin](https://github.com/openjfx/javafx-maven-plugin).

After following the README for the JavaFX Maven Plugin you will end up with something like:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>12.0.2</version>
    </dependency>
</dependencies>

<plugins>
    <plugin>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-maven-plugin</artifactId>
        <version>0.0.3</version>
        <configuration>
            <mainClass>hellofx/org.openjfx.App</mainClass>
        </configuration>
    </plugin>
</plugins>
```

Have a look at [Maven Central's org.openjfx](https://mvnrepository.com/artifact/org.openjfx) entry for an overview of available modules.

### Test Framework
Next add a dependency corresponding to the testing framework you are using in your project. TestFX currently supports JUnit 4, JUnit 5, and Spock.

#### JUnit 4

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13-beta-3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-junit</artifactId>
    <version>4.0.18</version>
    <scope>test</scope>
</dependency>
```

#### JUnit 5

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.5.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-junit5</artifactId>
    <version>4.0.18</version>
    <scope>test</scope>
</dependency>
```

#### Spock

```xml
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
    <version>1.3-groovy-2.5</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-spock</artifactId>
    <version>4.0.18</version>
    <scope>test</scope>
</dependency>
```

### Matcher/Assertions Library
Finally you must add a dependency corresponding to the matcher/assertions libraries that you want to use with TestFX. TestFX currently supports Hamcrest matchers or AssertJ assertions.

#### Hamcrest
```xml
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest</artifactId>
    <version>2.1</version>
    <scope>test</scope>
</dependency>
```

#### AssertJ
```xml
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.13.2</version>
    <scope>test</scope>
</dependency>
```

## Examples

### Hamcrest Matchers
TestFX brings along a couple of custom Hamcrest matchers in package `org.testfx.matcher.*`.

### AssertJ based Assertions
TestFX uses its own AssertJ based assertion implementation class: `org.testfx.assertions.api.Assertions`.

#### JUnit 4 with Hamcrest Matchers

```java
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClickableButtonTest_JUnit4Hamcrest extends ApplicationTest {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        button = new Button("click me!");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    public void should_contain_button_with_text() {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("click me!"));
    }

    @Test
    public void when_button_is_clicked_text_changes() {
        // when:
        clickOn(".button");

        // then:
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
    }
}
```

#### JUnit 4 with AssertJ based Assertions 

```java
import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClickableButtonTest_JUnit4AssertJ extends ApplicationTest {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        button = new Button("click me!");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    public void should_contain_button_with_text() {
        Assertions.assertThat(button).hasText("click me!");
    }

    @Test
    public void when_button_is_clicked_text_changes() {
        // when:
        clickOn(".button");

        // then:
        Assertions.assertThat(button).hasText("clicked!");
    }
}
```

### JUnit 5 
TestFX uses [JUnit5's new extension mechanism](https://junit.org/junit5/docs/current/user-guide/#extensions) via `org.junit.jupiter.api.extension.ExtendWith`. By using this, implementors are not forced anymore to inherit from `ApplicationTest` and are free to choose their own super classes. 
  
It does also make use of [JUnit5's new dependency injection mechanism](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection). By using this, test methods have access to the `FxRobot` instance that must be used in order to execute actions within the UI.

##### JUnit 5 with Hamcrest Matchers

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class ClickableButtonTest_JUnit5Hamcrest {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void should_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat(button, LabeledMatchers.hasText("click me!"));
        // or (lookup by css id):
        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("click me!"));
        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("click me!"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void when_button_is_clicked_text_changes(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // then:
        FxAssert.verifyThat(button, LabeledMatchers.hasText("clicked!"));
        // or (lookup by css id):
        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("clicked!"));
        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
    }
}
```

#### JUnit 5 with AssertJ Assertions

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class ClickableButtonTest_JUnit5AssertJ {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void should_contain_button_with_text(FxRobot robot) {
        Assertions.assertThat(button).hasText("click me!");
        // or (lookup by css id):
        Assertions.assertThat(robot.lookup("#myButton").queryAs(Button.class)).hasText("click me!");
        // or (lookup by css class):
        Assertions.assertThat(robot.lookup(".button").queryAs(Button.class)).hasText("click me!");
        // or (query specific type):
        Assertions.assertThat(robot.lookup(".button").queryButton()).hasText("click me!");
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void when_button_is_clicked_text_changes(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // then:
        Assertions.assertThat(button).hasText("clicked!");
        // or (lookup by css id):
        Assertions.assertThat(robot.lookup("#myButton").queryAs(Button.class)).hasText("clicked!");
        // or (lookup by css class):
        Assertions.assertThat(robot.lookup(".button").queryAs(Button.class)).hasText("clicked!");
        // or (query specific type)
        Assertions.assertThat(robot.lookup(".button").queryButton()).hasText("clicked!");
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
        testCompile "org.testfx:openjfx-monocle:8u76-b04" // jdk-9+181 for Java 9, jdk-11+26 for Java 11
    }
    ```

    `pom.xml`
    ```xml
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>openjfx-monocle</artifactId>
        <version>8u76-b04</version> <!-- jdk-9+181 for Java 9, jdk-11+26 for Java 11 -->
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
        - $HOME/.m2
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
