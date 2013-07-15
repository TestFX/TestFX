TestFX
======

TestFX is an easy to use library for testing JavaFX.

### Usage Examples

```java
class FileManagerTest extends TestFX
{
  public void trashCan_should_acceptFiles()
  {
    click( "#foo" ).type( "Hello world!" ).push( ENTER )
  
    drag( ".list-item .handle" ).to( "#trash-can" );
    
  
    assertThat( "#my-dialog .confirm-button", exists() )
  
    assertThat( "#tweet-lane", contains( 2, ".tweet" ) );
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
