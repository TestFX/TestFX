# Robot Component

_This manual page is an early draft. The class `FxRobot` is annotated with `@Beta`, thus might be subject to change._


## Introduction

*TBD.*


## Concepts

*TBD.*


## Reference

**IMPLEMENTATION OF KEYBOARD AND MOUSE ROBOT.**

Examples | Description
-------- | -----------
`press(F1)`<br>`press(CONTROL, SHIFT)`<br>`press(PRIMARY)`<br>`press(PRIMARY, SECONDARY)` | `KeyCode...` or `Mouse...`
`release(F1)`<br>`release(CONTROL, SHIFT)`<br>`release(PRIMARY)`<br>`release(PRIMARY, SECONDARY)` | `KeyCode...` or `Mouse...`
`releaseKeys()` |
`releaseButtons()` |

**IMPLEMENTATION OF TYPE AND WRITE ROBOT.**

Examples | Description
-------- | -----------
`push(SHIFT, ENTER)`<br>`push(keyCodeCombination)` | `KeyCode...`, `KeyCodeCombination`
`type(NUMPAD8, MINUS, NUMPAD2, ENTER)`<br>`type(DOWN, UP)` | `KeyCode...`
`type(ENTER, 5)` | `KeyCode` `int`
`write('a')`<br>`write("typewriter")`<br>`write("abc áèö\n")` | `char`, `String`

**IMPLEMENTATION OF MOVE ROBOT.**

Examples | Description
-------- | -----------
`moveTo()` | `PointQuery`
`moveTo()` | `x` and `y`, `Point2D`, `Bounds`
`moveTo()` | `Node`, `Scene`, `Window`
`moveTo()` | `String`, `Matcher`, `Predicate`
`moveBy(50, 50)`<br>`moveBy(-100, 0)` | `x` and `y`

**IMPLEMENTATION OF CLICK ROBOT.**

Examples | Description
-------- | -----------
`click()`<br>`click(PRIMARY)` | `MouseButton...`
`clickOn(pointQuery)` | `PointQuery`
`clickOn(100, 200)`<br>`clickOn(new Point2D(100, 200))`<br>`clickOn(new BoundingBox(0, 0, 50, 50))` | two `double`s, `Point2D`, `Bounds`
`clickOn(node)`<br>`clickOn(scene)`<br>`clickOn(window)` | `Node`, `Scene`, `Window`
`clickOn("#okButton")`<br>`clickOn(LabeledMatchers.hasText("Ok"))`<br>`clickOn((Button b) -> b.isDefault())` | `String`, `Matcher`, `Predicate`
`rightClick()` | `MouseButton...`
`rightClickOn()` | `PointQuery`
`rightClickOn()` | `x` and `y`, `Point2D`, `Bounds`
`rightClickOn()` | `Node`, `Scene`, `Window`
`rightClickOn()` | `String`, `Matcher`, `Predicate`
`doubleClick()` | `MouseButton...`
`doubleClickOn()` | `PointQuery`
`doubleClickOn()` | `x` and `y`, `Point2D`, `Bounds`
`doubleClickOn()` | `Node`, `Scene`, `Window`
`doubleClickOn()` | `String`, `Matcher`, `Predicate`

**IMPLEMENTATION OF DRAG ROBOT.**

Examples | Description
-------- | -----------
`drag()`<br>`drag(SECONDARY)` | `MouseButton...`
`dragFrom(pointQuery)` | `PointQuery`
`dragFrom(100, 200)`<br>`dragFrom(new Point2D(100, 200))`<br>`dragFrom(new BoundingBox(0, 0, 50, 50))` | `x` and `y`, `Point2D`, `Bounds`
`dragFrom(node)`<br>`dragFrom(scene)`<br>`dragFrom(stage)`<br>`dragFrom(window)` | `Node`, `Scene`, `Window`
`dragFrom("#okButton")`<br>`dragFrom("#mainPane .label")`<br>`dragFrom("Submit")`<br>`dragFrom(NodeMatchers.isEnabled())`<br>`dragFrom((node) -> !node.isDisabled())`<br>`dragFrom((Button b) -> !b.isDisabled())` | `String`, `Matcher`, `Predicate`
`drop()` |
`dropTo(pointQuery)` | `PointQuery`
`dropTo(100, 200)`<br>`dropTo(new Point2D(100, 200))`<br>`dropTo(new BoundingBox(0, 0, 50, 50))` | `x` and `y`, `Point2D`, `Bounds`
`dropTo(node)`<br>`dropTo(scene)`<br>`dropTo(stage)`<br>`dropTo(window)` | `Node`, `Scene`, `Window`
`dropTo("#okButton")`<br>`dropTo("#mainPane .label")`<br>`dropTo("Submit")`<br>`dropTo(NodeMatchers.isEnabled())`<br>`dropTo((node) -> !node.isDisabled())`<br>`dropTo((Button b) -> !b.isDisabled())` | `String`, `Matcher`, `Predicate`
`dropBy(50, 50)`<br>`dropBy(-100, 0)` | `x` and `y`

**IMPLEMENTATION OF SCROLL ROBOT.**

Examples | Description
-------- | -----------
`scroll(UP)`<br>`scroll(DOWN)` | `VerticalDirection`
`scroll(UP, 5)`<br>`scroll(DOWN, 5)` | `VerticalDirection`, `int`

**IMPLEMENTATION OF SLEEP ROBOT.**

Examples | Description
-------- | -----------
`sleep(250)` | `long`
`sleep(1, SECONDS)` | `long`, `TimeUnit`

**METHODS FOR NODE LOOKUP.**

`verifyThat(node(anything()), hasItem(hasText("Start"))`

Examples | Description
-------- | -----------
`node("#okButton")`<br>`node("#mainPane .label")`<br>`node("Submit")`<br>`node(Matchers.anything())`<br>`node(NodeMatchers.isEnabled())`<br>`node((node) -> !node.isDisabled())`<br>`node((Button b) -> !b.isDisabled())` | returns `NodeQuery`. `String`, `Predicate`, `Matcher`
`node(queryString, parentNode)`<br>`node(matcher, parentNode)`<br>`node(predicate, parentNode)` | returns `NodeQuery`. `String`, `Predicate`, `Matcher` and `Node...` parent nodes
`rootNode(node)`<br>`rootNode(scene)`<br>`rootNode(window)` | `Window`, `Scene`, `Node`
`rootNode(0)`<br>`rootNode(".+FX")`<br>`rootNode(Pattern.compile(".+FX"))`<br>`rootNode((Window w) -> w.isShowing())` | `Predicate<Window>`, `int`, `Pattern`, `String`
`queryNode()` | returns `RawNodeQuery` with `from()`, `lookup()`, `lookupAt()`, `match()`

**METHODS FOR POINT POSITION, LOCATION, OFFSET.**

Examples | Description
-------- | -----------
`targetPos()` | `Pos`
`point()` | returns `PointQuery`. `x` and `y`, `Point2D`, `Bounds`
`point()` | returns `PointQuery`. `Node`, `Scene`, `Window`
`point()` | returns `PointQuery`. `String`, `Matcher`, `Predicate`
`offset()` | returns `PointQuery`. `x` and `y`, `Point2D`, `Bounds`
`offset()` | returns `PointQuery`. `Node`, `Scene`, `Window`
`offset()` | returns `PointQuery`. `String`, `Matcher`, `Predicate`
`queryPoint()` | returns `RawPointQuery` with `relativeTo(PointQuery)`

**METHODS FOR WINDOW TARGETING, LOOKUP.**

Examples | Description
-------- | -----------
`targetWindow()` | `Window`, `Scene`, `Node`
`targetWindow()` | `int`, `Pattern`, `String`,
`window()` | `Scene`, `Node`
`window()` | `Predicate<Window>`, `int`, `Pattern`, `String`,
`listWindows()` | returns `List<Window>`
`listWindowsInQueue()` | returns `List<Window>`
