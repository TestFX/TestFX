# TestFX

[![Build Status](http://travis-ci.org/TestFX/TestFX.svg?branch=master)](https://travis-ci.org/TestFX/TestFX)
[![Bintray Version](https://api.bintray.com/packages/testfx/testfx/testfx-core/images/download.svg)](https://bintray.com/testfx/testfx)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.testfx/testfx-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.testfx/testfx-core)

Simple and clean testing for [JavaFX][10].

[10]: http://www.oracle.com/us/technologies/java/fx/overview/index.html


## Status

Version 4.0.0 is in alpha phase. Release notes are listed in [CHANGES.md](CHANGES.md) and latest documentation is only available via `gradle javadoc`.


## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers to verify expected states.

**Support for:**

- Java 8 features and JavaFX 8 controls.
- The JUnit testing framework and Hamcrest matchers.
- Screenshots of failed tests.
- Headless testing using Monocle.


## Example

```java
public class DesktopPaneTest extends FxRobotTestBase {
    @Before
    public void setup() throws Exception {
        setupSceneRoot(() -> new DesktopPane());
        showStage();
    }

    @Test
    public void should_drag_file_to_trashcan() {
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


## Maven and Gradle

Gradle `build.gradle`:
~~~groovy
dependencies {
    testCompile "org.testfx:testfx-core:4.0.0-SNAPSHOT"
}

repositories {
    maven { 
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}
~~~


## Documentation

- [How to use TestFX in your project][100]
- [How to build and deploy TestFX][101]
- [How to contribute][102]

[100]: https://github.com/TestFX/TestFX/wiki/How-to-use-TestFX-in-your-project
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


## Mailing List

Head over to [testfx-discuss@googlegroups.com][50] for discussions, questions and announcements.

[50]: https://groups.google.com/d/forum/testfx-discus


## Credits

TestFX was initially created by @dainnilsson and @minisu as a part of LoadUI in 2012. Today, it is being extended and maintained by @hastebrot, @Philipp91, @minisu and the [other contributors][60].

[60]: https://github.com/TestFX/TestFX/graphs/contributors "Contributors of LoadUI"


## License

~~~
Copyright 2013-2014 SmartBear Software
Copyright 2014-2015 The TestFX Contributors

Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
European Commission - subsequent versions of the EUPL (the "Licence"); You may
not use this work except in compliance with the Licence.

You may obtain a copy of the Licence at:
http://ec.europa.eu/idabc/eupl

Unless required by applicable law or agreed to in writing, software distributed
under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
specific language governing permissions and limitations under the Licence.
~~~
