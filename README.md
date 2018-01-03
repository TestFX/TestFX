# TestFX

[![Travis CI](https://img.shields.io/travis/TestFX/TestFX/master.svg?label=travis&style=flat-square)](https://travis-ci.org/TestFX/TestFX)
[![AppVeyor Build Status](https://img.shields.io/appveyor/ci/testfx/testfx/master.svg?style=flat-square)](https://ci.appveyor.com/project/testfx/testfx/branch/master)
[![Coveralls Test Coverage](https://img.shields.io/coveralls/github/TestFX/TestFX/master.svg?style=flat-square)](https://coveralls.io/github/TestFX/TestFX)
[![Bintray JCenter](https://img.shields.io/bintray/v/testfx/testfx/testfx-core.svg?label=bintray&style=flat-square)](https://bintray.com/testfx/testfx)
[![Maven Central](https://img.shields.io/maven-central/v/org.testfx/testfx-core.svg?label=maven&style=flat-square)](https://search.maven.org/#search|ga|1|org.testfx)
[![Chat on Gitter](https://img.shields.io/gitter/room/testfx/testfx-core.svg?style=flat-square)](https://gitter.im/TestFX/TestFX)

Simple and clean testing for JavaFX.

## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers to verify expected states of JavaFX widgets.

**Support for:**

- Java 8 features and JavaFX 8 controls.
- Multiple testing frameworks ([Junit 4](http://junit.org/junit4/), [Junit 5](http://junit.org/junit5/), and [Spock](http://spockframework.org/)) with Hamcrest matchers.
- Screenshots of failed tests.
- Headless testing using [Monocle](https://github.com/TestFX/Monocle).

## Gradle

To add a dependency on TestFX using Gradle, use the following:

```gradle
dependencies {
    testCompile "org.testfx:testfx-core:4.0.10-alpha"
}
```

Next add a dependency corresponmding to the testing framework you are using in
your project. TestFX supports JUnit 4, JUnit 5, and Spock. For example if
you are using JUnit 4 in your project you would add a dependency on `testfx-junit`:

```gradle
dependencies {
    testCompile "org.testfx:testfx-junit:4.0.10-alpha"
}
```

### Java 9

To use TestFX with Java 9 the `testfx-core` dependency must be tweaked by excluding
`testfx-internal-java8` and adding a dependency on `testfx-internal-java9` thusly:

```gradle
testCompile('org.testfx:testfx-core:4.0.10-alpha') {
    exclude group: 'org.testfx', module: 'testfx-internal-java8'
}
testRuntime 'org.testfx:testfx-internal-java9:4.0.10-alpha'
```

There are plans to make this easier in a future version of TestFX, perhaps
by utilizing multi-release JARs.

## Maven

To add a dependency on TestFX using Maven, use the following:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.10-alpha</version>
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
    <version>4.0.10-alpha</version>
    <scope>test</scope>
</dependency>
```

### Java 9

To use TestFX with Java 9 the `testfx-core` dependency must be tweaked by excluding
`testfx-internal-java8` and adding a dependency on `testfx-internal-java9` thusly:

```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.10-alpha</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.testfx</groupId>
            <artifactId>testfx-internal-java8</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-internal-java9</artifactId>
    <version>4.0.10-alpha</version>
    <scope>test</scope>
</dependency>
```

There are plans to make this easier in a future version of TestFX, perhaps
by utilizing multi-release JARs.

## Example (JUnit 4)

```java
public class DesktopPaneTest extends ApplicationTest {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new DesktopPane(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void should_drag_file_into_trashcan() {
        // given:
        rightClickOn("#desktop").moveTo("New").clickOn("Text Document");
        write("myTextfile.txt").push(ENTER);

        // when:
        drag(".file").dropTo("#trash-can");

        // then:
        verifyThat("#desktop", hasChildren(0, ".file"));
    }
}
```

### Example (JUnit 5)

```java
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

## Example (Spock)

```java
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

## Credits

Thanks to all of the [contributors of TestFX](https://github.com/TestFX/TestFX/graphs/contributors)!
