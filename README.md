TestFX
======

TestFX is an easy-to-use library for testing [JavaFX](http://www.oracle.com/us/technologies/java/fx/overview/index.html). TestFX provides:

 - A fluent and clean API.
 - Hamcrest Matchers for JavaFX.
 - Screenshots of failed tests.

### Usage Example

```java
class DesktopTest extends GuiTest
{
  @Before
  public void createNewFileOnDesktop()
  {
    // GIVEN
    showNodeInStage( aNode );
  
    rightClick( "#desktop" ).moveMouseTo( "New" ).click( "Text Document" ).
                             type( "myTextfile.txt" ).push( ENTER );
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


### Getting Started
To start using TestFX, you have to add the following elements to your pom.xml file:
```XML
<pluginRepositories>
   <pluginRepository>
      <id>loaduiRepository</id>
      <url>http://www.soapui.org/repository/</url>
   </pluginRepository>
</pluginRepositories>
```
```XML
<dependency>
    <groupId>org.loadui</groupId>
    <artifactId>testFx</artifactId>
    <version>2.6.1</version>
</dependency>
```
You will also have to set `forkMode` to _always_ in your surefire configuration:
```XML
<build>
   <plugins>
		    <plugin>
			      <groupId>org.apache.maven.plugins</groupId>
			      <artifactId>maven-surefire-plugin</artifactId>
			      <version>2.14.1</version>
			      <configuration>
				        <forkMode>always</forkMode>
			      </configuration>
		    </plugin>
   </plugins>
</build>
```

After that, head over to [the documentation][7].

You might also be interested in this [conference session][8] at JavaOne featuring TestFX.

### Motivation
The motivation for creating TestFX was that the existing library for testing JavaFX, [Jemmy][1], was
too verbose and unwieldy. We wanted more behavior driven tests with easy-to-read code that our tester could follow and modify on her own.
Today, TestFX is used in all of the about 50 [automated GUI tests in LoadUI][9].

[Comparison with Jemmy][4]

If TestFX does not suit your needs, you should have a look at Jemmy or [MarvinFX][6].

### Credits
TestFX was initially created by @dainnilsson and @minisu as a part of [LoadUI][2] in 2012. Today, it is being extended
and maintained by the [LoadUI team][5].

[1]: https://jemmy.java.net/              "Jemmy website"
[2]: https://github.com/SmartBear/loadui  "LoadUI project at Github"
[3]: http://www.oracle.com/technetwork/java/javafx/overview/index.html "JavaFX website"
[4]: https://github.com/SmartBear/TestFX/wiki/Comparison-with-Jemmy "Comparison with Jemmy"
[5]: https://github.com/SmartBear/loadui/graphs/contributors "Contributors of LoadUI"
[6]: https://github.com/guigarage/MarvinFX "MarvinFX's project page on Github"
[7]: https://github.com/SmartBear/TestFX/wiki/Documentation "Documentation"
[8]: https://oracleus.activeevents.com/2013/connect/sessionDetail.ww?SESSION_ID=2670 "Ten Man-Years of JavaFX: Real-World Project Experiences [CON2670]"
[9]: https://github.com/SmartBear/loadui/tree/master/loadui-project/loadui-fx-interface/src/test/java/com/eviware/loadui/ui/fx "GUI tests in LoadUI"
