# TestFX

[![Travis CI](https://img.shields.io/travis/TestFX/TestFX/master.svg?label=travis&style=flat-square)](https://travis-ci.org/TestFX/TestFX)
[![Bintray JCenter](https://img.shields.io/bintray/v/testfx/testfx/testfx-core.svg?label=bintray&style=flat-square)](https://bintray.com/testfx/testfx)
[![Maven Central](https://img.shields.io/maven-central/v/org.testfx/testfx-core.svg?label=maven&style=flat-square)](https://search.maven.org/#search|ga|1|org.testfx)
[![Chat on Gitter](https://img.shields.io/gitter/room/testfx/testfx-core.svg?style=flat-square)](https://gitter.im/TestFX/TestFX)

Simple and clean testing for [JavaFX][10].

[10]: http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html


## Status

Version 4 is in alpha phase. Release notes are listed in [`CHANGES.md`](CHANGES.md) and latest documentation is only available via `gradle javadoc`.


## Features

- A fluent and clean API.
- Flexible setup and cleanup of JavaFX test fixtures.
- Simple robots to simulate user interactions.
- Rich collection of matchers to verify expected states.

**Support for:**

- Java 8 features and JavaFX 8 controls.
- JUnit testing framework and Hamcrest matchers.
- Precise screenshots of failed tests.
- Headless testing using Monocle.


## Example

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


## Gradle and Maven

Gradle `build.gradle` with `testfx-core` and `testfx-junit` from Bintray's JCenter (at https://jcenter.bintray.com/):

~~~groovy
repositories {
    jcenter()
}

dependencies {
    testCompile "org.testfx:testfx-core:4.0.+"
    testCompile "org.testfx:testfx-junit:4.0.+"
}
~~~

Gradle `build.gradle` with `testfx-core`, `testfx-legacy` and a different `junit` version from Maven Central repository (at https://repo1.maven.org/maven2/).

~~~groovy
repositories {
    mavenCentral()
}

dependencies {
    testCompile "junit:junit:4.10"
    testCompile "org.testfx:testfx-core:4.0.+"
    testCompile "org.testfx:testfx-legacy:4.0.+", {
        exclude group: "junit", module: "junit"
    }
}
~~~

Gradle `build.gradle` with `testfx-core` (SNAPSHOT) from Sonatype Snapshots repository (at https://oss.sonatype.org/content/repositories/snapshots/).

~~~groovy
repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    testCompile "junit:junit:4.12"
    testCompile "org.testfx:testfx-core:4.0.0-SNAPSHOT"
}
~~~

Maven `pom.xml` with `testfx-core` from Maven Central repository (at https://repo1.maven.org/maven2/).

~~~xml
<repositories>
    <repository>
        <id>maven-central-repo</id>
        <url>http://repo1.maven.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-core</artifactId>
        <version>4.0.1-alpha</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-junit</artifactId>
        <version>4.0.1-alpha</version>
        <scope>test</scope>
    </dependency>
</dependencies>
~~~

Maven `pom.xml` with `testfx-core` (SNAPSHOT) from Sonatype Snapshots repository (at https://oss.sonatype.org/content/repositories/snapshots/).

~~~xml
<repositories>
    <repository>
        <id>maven-central-repo</id>
        <url>http://repo1.maven.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>sonatype-snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
       </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-core</artifactId>
        <version>4.0.0-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>
</dependencies>
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

[50]: https://groups.google.com/d/forum/testfx-discuss


## Credits

TestFX was initially created by @dainnilsson and @minisu as a part of LoadUI in 2012. Today, it is being extended and maintained by @hastebrot, @Philipp91, @minisu and the [other contributors][60].

[60]: https://github.com/TestFX/TestFX/graphs/contributors "Contributors of TestFX"


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
