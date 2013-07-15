TestFX
======

TestFX is an easy to use framework for testing JavaFX.

### Usage Examples

```java
click( "#foo" ).type( "Hello world!" ).push( ENTER )

drag( ".list-item .handle" ).to( "#trash-can" );


assertThat( "#my-dialog .confirm-button", exists() )

assertThat( "#tweet-lane", contains( 2, ".tweet" ) );
```


### Hamcrest Matchers
Hamcrest matchers for JavaFX.


#### Usage Examples

```java
assertThat( "#my-dialog .confirm-button", exists() )

assertThat( "#tweet-lane", contains( 2, ".tweet" ) );
```

### Credits
TestFX was initially created by @dainnilsson as a part of [LoadUI][1]. Today, it is being extended
and maintained by the LoadUI team.

[1]: https://github.com/SmartBear/loadui        "LoadUI project at Github"
