<!-- tag: v4.0.0-alpha -->
## TestFX 4.0.0-alpha &mdash; February 27, 2015

322 commits by 13 authors:
- **Benjamin Gudehus** ([@hastebrot](https://github.com/hastebrot)) &mdash; 277 commits
- **Henrik Olsson** &mdash; 11 commits
- **Sven Ruppert** ([@svenruppert](https://github.com/svenruppert)) &mdash; 10 commits
- **Andres Almiray** ([@aalmiray](https://github.com/aalmiray)) &mdash; 6 commits
- **Jan Gassen** ([@0x4a616e](https://github.com/0x4a616e)), **Naimdjon Takhirov** ([@naimdjon](https://github.com/naimdjon)) &mdash; 4 commits
- **Diego Cirujano** ([@Curuman](https://github.com/Ciruman)) &mdash; 3 commits
- **Grégory Fernandez** ([@lgringo](https://github.com/lgringo)) &mdash; 2 commits
- **Andrea Vacondio** ([@torakiki](https://github.com/torakiki)), **Johannes Mockenhaupt** ([@jotomo](https://github.com/jotomo)), **Making GitHub Delicious.** ([@waffle-iron](https://github.com/waffle-iron)), **Robert Ladstätter** ([@rladstaetter](https://github.com/rladstaetter)), **Sergei Ivanov** ([@sergei-ivanov](https://github.com/sergei-ivanov)) &mdash; 1 commit

65 merged pull requests:
- **(chore)** Gradle: Add project.jfxrtLocation to test.classpath. ([#120](https://github.com/TestFX/TestFX/pull/120)) &mdash; 2 commits
- **(chore)** Gradle: Build now runs with `gradle clean jar javadocJar sourceJar test check`. ([#88](https://github.com/TestFX/TestFX/pull/88)) &mdash; 1 commit
- **(chore)** Gradle: Cleanup build scripts. ([#201](https://github.com/TestFX/TestFX/pull/201)) &mdash; 13 commits
- **(chore)** Gradle: Fix missing Gradle wrapper. ([#117](https://github.com/TestFX/TestFX/pull/117)) &mdash; 2 commits
- **(chore)** Gradle: Reintroduce BinTray support. ([#128](https://github.com/TestFX/TestFX/pull/128)) &mdash; 1 commit
- **(chore)** Gradle: Remove JUnit compile dependency and update Guava. ([#197](https://github.com/TestFX/TestFX/pull/197)) &mdash; 3 commits
- **(chore)** Gradle: Rerun `gradle wrapper`. ([#116](https://github.com/TestFX/TestFX/pull/116)) &mdash; 1 commit
- **(chore)** Gradle: Temporarily remove BinTray support to fix Gradle view refresh in IntelliJ IDEA. ([#127](https://github.com/TestFX/TestFX/pull/127)) &mdash; 1 commit
- **(chore)** Travis: Add `.travis.yml`. ([#118](https://github.com/TestFX/TestFX/pull/118)) &mdash; 1 commit
- **(chore)** Travis: Try to use xvfb (X Virtual Framebuffer). ([#119](https://github.com/TestFX/TestFX/pull/119)) &mdash; 1 commit
- **(doc)** Add Unstable annotations to classes and methods. ([#203](https://github.com/TestFX/TestFX/pull/203)) &mdash; 1 commit
- **(doc)** Readme: Fix broken links to source code and update links to TestFX... ([#142](https://github.com/TestFX/TestFX/pull/142)) &mdash; 1 commit
- **(doc)** Readme: Update `README.md`. ([#196](https://github.com/TestFX/TestFX/pull/196)) &mdash; 2 commits
- **(doc)** Toolkit: Prepare docs and tests for candidate phase. ([#176](https://github.com/TestFX/TestFX/pull/176)) &mdash; 12 commits
- **(feat)** Allow instrumenting entire Applications. ([#183](https://github.com/TestFX/TestFX/pull/183)) &mdash; 4 commits
- **(feat)** Assert, Matcher: Implement type-aware matchers. ([#164](https://github.com/TestFX/TestFX/pull/164)) &mdash; 16 commits
- **(feat)** Assert, Matcher: Improve type-aware matchers. ([#195](https://github.com/TestFX/TestFX/pull/195)) &mdash; 11 commits
- **(feat)** Implement CallableBoundsPointQuery. ([#68](https://github.com/TestFX/TestFX/pull/68)) &mdash; 5 commits
- **(feat)** Lifecycle, Robot: Headless support. ([#159](https://github.com/TestFX/TestFX/pull/159)) &mdash; 27 commits
- **(feat)** Lifecycle: Implement LifecycleService. ([#156](https://github.com/TestFX/TestFX/pull/156)) &mdash; 12 commits
- **(feat)** ListViews provides a row selected matcher. ([#202](https://github.com/TestFX/TestFX/pull/202)) &mdash; 3 commits
- **(feat)** Robot: Add regression tests and create ClickRobotImpl. ([#97](https://github.com/TestFX/TestFX/pull/97)) &mdash; 1 commit
- **(feat)** Robot: Add regression tests and create DragRobotImpl. ([#98](https://github.com/TestFX/TestFX/pull/98)) &mdash; 1 commit
- **(feat)** Robot: Add regression tests and create MoveRobotImpl. ([#96](https://github.com/TestFX/TestFX/pull/96)) &mdash; 1 commit
- **(feat)** Robot: Implement adapters for Awt, Glass and JavaFX robots. ([#146](https://github.com/TestFX/TestFX/pull/146)) &mdash; 7 commits
- **(feat)** Robot: Support for Unicode strings in WriteRobot. ([#166](https://github.com/TestFX/TestFX/pull/166)) &mdash; 6 commits
- **(feat)** Service: Implement NodeQuery. ([#174](https://github.com/TestFX/TestFX/pull/174)) &mdash; 7 commits
- **(feat)** Util: Provide `invokeIn...()` and `waitFor...()` methods. ([#150](https://github.com/TestFX/TestFX/pull/150)) &mdash; 1 commit
- **(fix)** Add license header to MoveRobotImpl. ([#112](https://github.com/TestFX/TestFX/pull/112)) &mdash; 1 commit
- **(fix)** AppLauncher could not retrieve primary Stage on OSX. ([#126](https://github.com/TestFX/TestFX/pull/126)) &mdash; 1 commit
- **(fix)** Compiler warnings for deprecated JUnit methods. ([#104](https://github.com/TestFX/TestFX/pull/104)) &mdash; 1 commit
- **(fix)** Deprecation and compile warnings caused by dependency upgrades. ([#198](https://github.com/TestFX/TestFX/pull/198)) &mdash; 4 commits
- **(fix)** Matcher: nodeHasLabel() does not perform instance check for Text. ([#124](https://github.com/TestFX/TestFX/pull/124)) &mdash; 1 commit
- **(fix)** Service: Calls to `Point2D#add()` incompatible with Java 7. ([#87](https://github.com/TestFX/TestFX/pull/87)) &mdash; 1 commit
- **(fix)** Service: Fix compatibility to Java 7 and JavaFX 2. ([#103](https://github.com/TestFX/TestFX/pull/103)) &mdash; 1 commit
- **(fix)** Util: Only start waiting for boolean observable when it is false. ([#151](https://github.com/TestFX/TestFX/pull/151)) &mdash; 2 commits
- **(refactor)** API: Introduce FxSelector and FxSelectorContext. ([#165](https://github.com/TestFX/TestFX/pull/165)) &mdash; 2 commits
- **(refactor)** API: Move FxRobot to org.testfx.api. ([#171](https://github.com/TestFX/TestFX/pull/171)) &mdash; 5 commits
- **(refactor)** API: Move some org.loadui.testfx classes to org.testfx. ([#172](https://github.com/TestFX/TestFX/pull/172)) &mdash; 9 commits
- **(refactor)** API: Rename pointFor() to point() and add pointOfVisibleNode(). ([#194](https://github.com/TestFX/TestFX/pull/194)) &mdash; 2 commits
- **(refactor)** Extract robot and service classes from GuiTest. ([#39](https://github.com/TestFX/TestFX/pull/39)) &mdash; 66 commits
- **(refactor)** Framework: Extract ToolkitApplication. ([#153](https://github.com/TestFX/TestFX/pull/153)) &mdash; 1 commit
- **(refactor)** Legacy: Move org.loadui.testfx to testfx-legacy module. ([#192](https://github.com/TestFX/TestFX/pull/192)) &mdash; 2 commits
- **(refactor)** Robot: Add regression tests and refactor KeyboardRobotImpl. ([#101](https://github.com/TestFX/TestFX/pull/101)) &mdash; 1 commit
- **(refactor)** Robot: Add regression tests and refactor MouseRobotImpl. ([#94](https://github.com/TestFX/TestFX/pull/94)) &mdash; 1 commit
- **(refactor)** Robot: Add regression tests and refactor ScrollRobotImpl. ([#100](https://github.com/TestFX/TestFX/pull/100)) &mdash; 1 commit
- **(refactor)** Robot: Extract mouse methods to MouseRobotImpl and rename ... ([#149](https://github.com/TestFX/TestFX/pull/149)) &mdash; 1 commit
- **(refactor)** Robot: Refactor FxRobot. ([#105](https://github.com/TestFX/TestFX/pull/105)) &mdash; 1 commit
- **(refactor)** Robot: Refactor TypeRobotImpl. ([#102](https://github.com/TestFX/TestFX/pull/102)) &mdash; 1 commit
- **(refactor)** Robot: Revise Robots related to keyboard inputs. ([#147](https://github.com/TestFX/TestFX/pull/147)) &mdash; 10 commits
- **(refactor)** Toolkit: Prepare API for beta phase. ([#170](https://github.com/TestFX/TestFX/pull/170)) &mdash; 6 commits
- **(refactor)** Util: Enhance RunWaitUtils. ([#154](https://github.com/TestFX/TestFX/pull/154)) &mdash; 4 commits
- **(test)** Move ScrollPaneTest from unit tests to integration tests. ([#99](https://github.com/TestFX/TestFX/pull/99)) &mdash; 1 commit
- #110: jcenter is not supported in gradle < 1.7 ([#113](https://github.com/TestFX/TestFX/pull/113)) &mdash; 3 commits
- Extract static methods from FxRobot to GuiTest ([#91](https://github.com/TestFX/TestFX/pull/91)) &mdash; 3 commits
- Fix NP in containsCell in TableViews ([#67](https://github.com/TestFX/TestFX/pull/67)) &mdash; 1 commit
- Fix scroll direction as UP was going down (and DOWN up). ([#95](https://github.com/TestFX/TestFX/pull/95)) &mdash; 2 commits
- Misc fixes + bumped version. ([#61](https://github.com/TestFX/TestFX/pull/61)) &mdash; 5 commits
- No longer fixed scene size. ([#62](https://github.com/TestFX/TestFX/pull/62)) &mdash; 1 commit
- Respect headless mode instead of enforcing it ([#137](https://github.com/TestFX/TestFX/pull/137)) &mdash; 1 commit
- Setup a multi-project Gradle build ([#71](https://github.com/TestFX/TestFX/pull/71)) &mdash; 4 commits
- Update HasLabelStringMatcher.java ([#108](https://github.com/TestFX/TestFX/pull/108)) &mdash; 1 commit
- Update build files ([#122](https://github.com/TestFX/TestFX/pull/122)) &mdash; 2 commits
- make timeout configurable ([#133](https://github.com/TestFX/TestFX/pull/133)) &mdash; 1 commit
- waffle.io Badge ([#134](https://github.com/TestFX/TestFX/pull/134)) &mdash; 1 commit

<!-- tag: v3.1.2 -->
## TestFX 3.1.2 &mdash; January 30, 2014

14 commits by 3 authors:
- **Henrik Olsson** &mdash; 10 commits
- **Henrik Stråth** ([@minisu](https://github.com/minisu)), **Philipp Keck** ([@Philipp91](https://github.com/Philipp91)) &mdash; 2 commits

4 merged pull requests:
- #43: push() does not wait between press/release calls ([#45](https://github.com/TestFX/TestFX/pull/45)) &mdash; 1 commit
- #47: getVisibleNodes() returns normal static Set ([#48](https://github.com/TestFX/TestFX/pull/48)) &mdash; 1 commit
- Now aborts test on user mouse movement. Related to #55 ([#59](https://github.com/TestFX/TestFX/pull/59)) &mdash; 1 commit
- Now releases keys/buttons on test aborted. #23 ([#60](https://github.com/TestFX/TestFX/pull/60)) &mdash; 1 commit

<!-- tag: v3.1.0 -->
## TestFX 3.1.0 &mdash; January 12, 2014

21 commits by 2 authors:
- **Henrik Olsson** &mdash; 14 commits
- **Henrik Stråth** ([@minisu](https://github.com/minisu)) &mdash; 5 commits
- **Philipp Keck** ([@Philipp91](https://github.com/Philipp91)) &mdash; 2 commits

1 merged pull request:
- Faster mouse movement over long distances ([#35](https://github.com/TestFX/TestFX/pull/35)) &mdash; 1 commit

<!-- tag: v3.0.0 -->
## TestFX 3.0.0 &mdash; December 23, 2013

155 commits by 6 authors:
- **Henrik Olsson** &mdash; 105 commits
- **Henrik Stråth** ([@minisu](https://github.com/minisu)) &mdash; 37 commits
- **Renato Athaydes** ([@renatoathaydes](https://github.com/renatoathaydes)) &mdash; 6 commits
- **Maximilian Skog** ([@maxonline](https://github.com/maxonline)) &mdash; 4 commits
- **Philipp Keck** ([@Philipp91](https://github.com/Philipp91)) &mdash; 2 commits
- **Bitdeli Chef** ([@bitdeli-chef](https://github.com/bitdeli-chef)) &mdash; 1 commit

2 merged pull requests:
- Add a Bitdeli Badge to README ([#26](https://github.com/TestFX/TestFX/pull/26)) &mdash; 1 commit
- Corrected verifyThat() to accept super-Matchers ([#22](https://github.com/TestFX/TestFX/pull/22)) &mdash; 2 commits
