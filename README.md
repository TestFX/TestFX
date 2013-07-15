TestFX
======

TestFX is an easy to use library for testing JavaFX.

### Usage Examples

```java
class FileManagerTest extends TestFX
{
  @Test
  public void shouldBeAbleToDragFileToTrashCan()
  {
    // GIVEN
    rightClick( "#desktop" ).moveTo( "#new" ).click( "#text-document" ).type( "myTextfile.txt" ).push( ENTER );
    assertThat( "#desktop", contains( 1, ".file" ) );
  
    // WHEN
    drag( ".file" ).to( "#trash-can" );
    
    // THEN
    assertThat( "#desktop", contains( 0, ".file" ) );
  }
}
```

### Motivation
Why not [Jemmy][1]? 

### Hamcrest Matchers
Hamcrest matchers for JavaFX.


### Credits
TestFX was initially created by @dainnilsson as a part of [LoadUI][2]. Today, it is being extended
and maintained by the LoadUI team.

[1]: https://jemmy.java.net/              "Jemmy website"
[2]: https://github.com/SmartBear/loadui  "LoadUI project at Github"
