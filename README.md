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

- Java 8 and 9.
- Multiple testing frameworks ([Junit 4](http://junit.org/junit4/), [Junit 5](http://junit.org/junit5/), and [Spock](http://spockframework.org/)).
- [Hamcrest](http://hamcrest.org/) matchers or [AssertJ](http://joel-costigliola.github.io/assertj/) assertions (or both!).
- Screenshots of failed tests.
- Headless testing using [Monocle](https://github.com/TestFX/Monocle).

## Gradle

To add a dependency on TestFX using Gradle, use the following:

```gradle
dependencies {
    testCompile "org.testfx:testfx-core:4.0.13-alpha"
}
```

Next add a dependency corresponding to the testing framework you are using in
your project. TestFX supports JUnit 4, JUnit 5, and Spock. For example if
you are using JUnit 4 in your project you would add a dependency on `testfx-junit`:

```gradle
dependencies {
    testCompile "org.testfx:testfx-junit:4.0.13-alpha"
}
```

## Maven

To add a dependency on TestFX using Maven, use the following:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.13-alpha</version>
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
    <version>4.0.13-alpha</version>
    <scope>test</scope>
</dependency>
```

## Example: JUnit 4 with Hamcrest Matchers

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

## Example: JUnit 4 with AssertJ Assertions

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

## Example: JUnit 5 with Hamcrest Matchers

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

## Example: JUnit 5 with AssertJ Assertions

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

## Example: Spock with Hamcrest Matchers

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
