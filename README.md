TestFX
======

Easy-to-use library for testing [JavaFX](http://www.oracle.com/us/technologies/java/fx/overview/index.html). TestFX provides:

 - A fluent and clean API for interacting with, and verifying the behavior of, JavaFX applications.
 - Supports Hamcrest Matchers and Lambda expressions.
 - Screenshots of failed tests.
 - Supports JavaFX 2 and JavaFX 8.

[Changelog](https://github.com/SmartBear/TestFX/wiki/Changelog)

### Usage Example

```java
class DesktopTest extends GuiTest
{
  public Parent getRootNode()
  {
    return new Desktop()
  }

  @Test
  public void shouldBeAbleToDragFileToTrashCan()
  {
    // GIVEN
    rightClick( "#desktop" ).move( "New" ).click( "Text Document" ).
                             type( "myTextfile.txt" ).push( ENTER );
  
    // WHEN
    drag( ".file" ).to( "#trash-can" );
    
    // THEN
    verifyThat( "#desktop", contains( 0, ".file" ) );
  }
}
```


### Documentation

 * [Getting started][11]
 * [Examples][17]
 * Reference
  * [Mouse control][12]
  * [Keyboard control][13]
  * [Assertions][14]
  * [Waiting][15]
  * Specific controls
    * [Labels][18]
    * [TableViews](https://github.com/SmartBear/TestFX/blob/master/src/test/java/org/loadui/testfx/TableViewsTest.java)
    * [ListViews](https://github.com/SmartBear/TestFX/blob/master/src/test/java/org/loadui/testfx/ListViewsTest.java)
    * [TextInputControls](https://github.com/SmartBear/TestFX/blob/master/src/test/java/org/loadui/testfx/TextInputControlsTest.java)
  * [Misc.][16]

### Links

 * [Using TestFX with Spock](https://github.com/SmartBear/TestFX/issues/38).
 * Conference sessions featuring TestFX: [JavaZone](http://jz13.java.no/presentation.html?id=89b56833) and [JavaOne][8].

### Motivation
The motivation for creating TestFX was that the existing library for testing JavaFX, [Jemmy][1], was
too verbose and unwieldy. We wanted more behavior driven tests with easy-to-read code that our tester could follow and modify on her own.
Today, TestFX is used in all of the about 100 automated GUI tests in LoadUI ([source code][9], [video][10]).

[Comparison with Jemmy][4]

### Mailing list
Head over to [testfx-discuss@googlegroups.com](https://groups.google.com/d/forum/testfx-discuss) for discussions, questions and announcements. 

### Credits
TestFX was initially created by @dainnilsson and @minisu as a part of [LoadUI][2] in 2012. Today, it is being extended and maintained by @minisu, @Philipp91 and the [other contributors][5].

[1]: https://jemmy.java.net/              "Jemmy website"
[2]: https://github.com/SmartBear/loadui  "LoadUI project at Github"
[3]: http://www.oracle.com/technetwork/java/javafx/overview/index.html "JavaFX website"
[4]: https://github.com/SmartBear/TestFX/wiki/Comparison-with-Jemmy "Comparison with Jemmy"
[5]: https://github.com/SmartBear/TestFX/graphs/contributors "Contributors of LoadUI"
[11]: https://github.com/SmartBear/TestFX/wiki/Getting-started
[12]: https://github.com/SmartBear/TestFX/wiki/Mouse-control
[13]: https://github.com/SmartBear/TestFX/wiki/Keyboard-control
[14]: https://github.com/SmartBear/TestFX/wiki/Assertions
[15]: https://github.com/SmartBear/TestFX/wiki/Waiting
[16]: https://github.com/SmartBear/TestFX/wiki/Misc
[17]: https://github.com/SmartBear/TestFX/wiki/Examples
[18]: https://github.com/SmartBear/TestFX/wiki/Login-form#using-textlabels
[8]: https://oracleus.activeevents.com/2013/connect/sessionDetail.ww?SESSION_ID=2670 "Ten Man-Years of JavaFX: Real-World Project Experiences [CON2670]"
[9]: https://github.com/SmartBear/loadui/tree/master/loadui-project/loadui-fx-interface/src/test/java/com/eviware/loadui/ui/fx "GUI tests in LoadUI"
[10]: http://youtu.be/fgD8fBn1cYw "Video of the LoadUI TestFX test suite"
