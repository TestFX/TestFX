TestFX
======

TestFX is an easy-to-use library for testing [JavaFX][2].

### Usage Example

```java
class DesktopTest extends GuiTest
{
  @Before
  public void createNewFileOnDesktop()
  {
    // GIVEN
    showNodeInStage( Desktop.class );
  
    rightClick( "#desktop" ).moveMouseTo( "New" ).click( "Text Document" ).type( "myTextfile.txt" ).push( ENTER );
    assertThat( "#desktop .file", hasLabel( "myTextFile.txt" ) );
  }

  @Test
  public void shouldBeAbleToDragFileToTrashCan()
  {
    // WHEN
    drag( ".file" ).to( "#trash-can" );
    
    // THEN
    assertThat( "#desktop", contains( 0, ".file" ) );
  }
}
```

### Motivation
The motivation for creating TestFX was that the existing library for testing JavaFX, [Jemmy][1], was
too verbose and unwieldy. We wanted more behavior driven tests with easy-to-read code that our tester could follow and modify on her own.

[Comparison with Jemmy][4]

If TestFX does not suit your needs, you should have a look at Jemmy or [MarvinFX][6].

### Start using
This is what you need in your pom.xml file to start using TestFX:
```XML
<pluginRepositories>
   <pluginRepository>
      <id>loaduiRepository</id>
      <url>http://www.soapui.org/repository/maven2/</url>
   </pluginRepository>
</pluginRepositories>
```
```XML
<dependency>
    <groupId>org.loadui</groupId>
    <artifactId>testFx</artifactId>
    <version>2.6.0</version>
</dependency>
```

### Credits
TestFX was initially created by @dainnilsson as a part of [LoadUI][2] in 2012. Today, it is being extended
and maintained by the [LoadUI team][5].

[1]: https://jemmy.java.net/              "Jemmy website"
[2]: https://github.com/SmartBear/loadui  "LoadUI project at Github"
[3]: http://www.oracle.com/technetwork/java/javafx/overview/index.html "JavaFX website"
[4]: https://github.com/SmartBear/TestFX/wiki/Comparison-with-Jemmy "Comparison with Jemmy"
[5]: https://github.com/SmartBear/loadui/graphs/contributors "Contributors of LoadUI"
[6]: https://github.com/guigarage/MarvinFX "MarvinFX's project page on Github"
