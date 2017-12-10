# TestFX

[![Travis CI](https://img.shields.io/travis/TestFX/TestFX/master.svg?label=travis&style=flat-square)](https://travis-ci.org/TestFX/TestFX)
[![AppVeyor Build Status](https://img.shields.io/appveyor/ci/testfx/testfx/master.svg?style=flat-square)](https://ci.appveyor.com/project/testfx/testfx/branch/master)
[![Coveralls Test Coverage](https://img.shields.io/coveralls/github/TestFX/TestFX/master.svg?style=flat-square)](https://coveralls.io/github/TestFX/TestFX)
[![Bintray JCenter](https://img.shields.io/bintray/v/testfx/testfx/testfx-core.svg?label=bintray&style=flat-square)](https://bintray.com/testfx/testfx)
[![Maven Central](https://img.shields.io/maven-central/v/org.testfx/testfx-core.svg?label=maven&style=flat-square)](https://search.maven.org/#search|ga|1|org.testfx)
[![Chat on Gitter](https://img.shields.io/gitter/room/testfx/testfx-core.svg?style=flat-square)](https://gitter.im/TestFX/TestFX)

Simple and clean testing for [JavaFX][10].

[10]: http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html


## Status

Version 4 is in alpha phase. Release notes are listed in [`CHANGES.md`](CHANGES.md). [Javadocs](http://testfx.github.io/TestFX/docs/javadoc/) are available
online (these are auto-generated from `master`) or by running `gradle javadoc`.

## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers to verify expected states.

**Support for:**

- Java 8 features and JavaFX 8 controls.
- Multiple testing frameworks ([Junit 4](http://junit.org/junit4/), [Junit 5](http://junit.org/junit5/), and [Spock](http://spockframework.org/)) with Hamcrest matchers.
- Precise screenshots of failed tests.
- Headless testing using Monocle.


## Example
The following example application is written for JUnit 4 only. [Here](https://github.com/TestFX/TestFX/blob/master/subprojects/testfx-junit5/src/test/java/org/testfx/framework/junit5/ApplicationRuleTest.java) is an example of a test with testfx-junit5.

~~~java
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
~~~


## Gradle

`build.gradle` with `testfx-core` and `testfx-junit` (Junit4) from Bintray's JCenter (at https://jcenter.bintray.com/):

~~~groovy
repositories {
    jcenter()
}

dependencies {
    testCompile "org.testfx:testfx-core:4.0.+"
    testCompile "org.testfx:testfx-junit:4.0.+"
}
~~~

For Junit5, use the `testfx-junit5` artifact instead of `testfx-junit`

## Maven

`pom.xml` with `testfx-core` and `testfx-junit` (Junit4) from Maven Central repository (at https://repo1.maven.org/maven2/).

~~~xml
<dependencies>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-core</artifactId>
        <version>4.0.8-alpha</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-junit</artifactId> <!-- or testfx-junit5 -->
        <version>4.0.8-alpha</version>
        <scope>test</scope>
    </dependency>
</dependencies>
~~~

For Junit5, use the `testfx-junit5` artifact instead of `testfx-junit`

## Documentation

- [How to build and deploy TestFX][101]
- [How to contribute][102]

[101]: https://github.com/TestFX/TestFX/wiki/How-to-build-and-deploy-TestFX
[102]: https://github.com/TestFX/TestFX/wiki/How-to-Contribute


## Motivation

The motivation for creating TestFX was that the existing library for testing JavaFX, Jemmy, was too verbose and unwieldy. We wanted more behavior driven tests with easy-to-read code that our tester could follow and modify on her own.

Today, TestFX is used in all of the about 100 automated GUI tests in LoadUI ([video][30]).

- [Comparison with Jemmy][31], in the TestFX wiki.
- *"Ten Man-Years of JavaFX: Real-World Project Experiences"*, conference session at [JavaZone][32] and [JavaOne][33].

[30]: http://youtu.be/fgD8fBn1cYw "Video of the LoadUI TestFX test suite"
[31]: https://github.com/TestFX/TestFX/wiki/Comparison-with-Jemmy "Comparison with Jemmy"
[32]: http://jz13.java.no/presentation.html?id=89b56833 "Ten man-years of JavaFX: Real-world project experiences"
[33]: https://oracleus.activeevents.com/2013/connect/sessionDetail.ww?SESSION_ID=2670 "Ten Man-Years of JavaFX: Real-World Project Experiences [CON2670]"


## Similar Frameworks

- [Jemmy][40], by the Oracle SQE team.
- [Automaton][41], a new testing framework that supports JavaFX as well as Swing.
- [MarvinFX][42], test support for JavaFX `Property`s.

[40]: https://jemmy.java.net/
[41]: https://github.com/renatoathaydes/Automaton
[42]: http://www.guigarage.com/2013/03/introducing-marvinfx/


## Gitter

Head over to our [gitter chat](https://gitter.im/TestFX/TestFX) for discussion and questions.


## Credits

TestFX was initially created by @dainnilsson and @minisu as a part of LoadUI in 2012. Today, it is being extended and maintained by @hastebrot, @Philipp91, @minisu and the [other contributors][60].

[60]: https://github.com/TestFX/TestFX/graphs/contributors "Contributors of TestFX"


## License

~~~
Copyright 2013-2014 SmartBear Software
Copyright 2014-2017 The TestFX Contributors

Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
European Commission - subsequent versions of the EUPL (the "Licence"); You may
not use this work except in compliance with the Licence.

You may obtain a copy of the Licence at:
http://ec.europa.eu/idabc/eupl.html

Unless required by applicable law or agreed to in writing, software distributed
under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
specific language governing permissions and limitations under the Licence.
~~~
