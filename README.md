TestFX
======

TestFX is an easy-to-use library for testing JavaFX.

### Example using CSS selectors

```java
class DesktopTest extends TestFX
{
  @BeforeClass
  public void setup()
  {
    // GIVEN
    rightClick( "#desktop" ).moveTo( "#new" ).click( "#text-document" ).type( "myTextfile.txt" ).push( ENTER );
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

### Example using Guava predicates

### Motivation
The motivation for creating TestFX was that the existing library for testing JavaFX, [Jemmy][1], was
too verbose and unwieldy. We wanted tests with easy-to-read code that our tester could follow and modify on her own.

### Credits
TestFX was initially created by @dainnilsson as a part of [LoadUI][2]. Today, it is being extended
and maintained by the LoadUI team.

[1]: https://jemmy.java.net/              "Jemmy website"
[2]: https://github.com/SmartBear/loadui  "LoadUI project at Github"
