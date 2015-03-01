# Toolkit Component

_This manual page is an early draft. The class `FxToolkit` is annotated with `@Beta`, thus might be subject to change._


## Introduction

This component is responsible for setup and cleanup of JavaFX test fixtures such as `Application`s, `Stage`s, `Scene`s and `Node`s.

Static methods in `org.testfx.api.FxToolkit` provide the primary API. Implemention details are found in the package `org.testfx.toolkit`.

In a normal test case `FxToolkit` setups the JavaFX fixtures, then [`FxRobot`](component-robot.md) simulates user interactions on them, after that [`FxAssert`](component-assert.md) checks their state and `FxToolkit` finally cleans the fixtures up.


## Concepts

Tests that reuse a single `Stage` (the primary `Stage`) for the whole test class.

~~~java
public class HelloPrimaryStageDemo {
    public static void main(String[] args) throws Exception {
        // before test class:
        FxToolkit.registerPrimaryStage();
        FxToolkit.showStage(); // show primary Stage, if was previously hidden.

        // before each test method:
        FxToolkit.setupStage(stage -> {
            stage.setScene(new Scene(new Label("within primary stage")));
        });
        FxToolkit.showStage();

        // on test suite complete (not needed in JUnit runs):
        Platform.exit();
    }
}
~~~

Tests that create a new `Stage` for each test method.

~~~java
public class HelloCustomStageDemo {
    public static void main(String[] args) throws Exception {
        // before test class:
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage(); // hide primary Stage, if was previously shown.

        // before each test method:
        FxToolkit.registerStage(() -> new Stage());
        FxToolkit.setupStage(stage -> {
            stage.setScene(new Scene(new Label("within custom stage")));
        });
        FxToolkit.showStage();

        // after each test method:
        FxToolkit.hideStage();

        // on test suite complete (not needed in JUnit runs):
        Platform.exit();
    }
}
~~~


## Reference

Static methods that **register stages as containers**.

Examples | Description
-------- | -----------
`registerPrimaryStage()` | Registers the primary `Stage` as the container for JavaFX fixtures. Launches the JavaFX toolkit, if not already launched.
`registerStage(() -> new Stage())` | Registers the `Stage` supplied with `Supplier<Stage>` as the container for JavaFX fixtures.

Static methods that **setup the contents of registered stages**.

Examples | Description
-------- | -----------
`setupStage(stage -> stage.setScene(scene)` | Provides the registered `Stage` to `Consumer<Stage>`.
`setupScene(() -> new Scene(new Region()))` | Uses the registered `Stage` to put the `Scene` supplied with `Supplier<Scene>` into it.
`setupSceneRoot(() -> new Region())` | Uses the registered `Stage` to put the `Parent` node supplied with `Supplier<Parent>` into it.
`setupApplication(SimpleDemo.class)`<br>`setupApplication(SimpleDemo.class, "--debug")` | Passes the registered `Stage` to `Application#start()` after creating and initializing the `Class<Application>`.
`cleanupApplication(application)` | Calls `Application#stop()` of the `Application`.

Static methods provided as **utility methods**.

Examples | Description
-------- | -----------
`setupFixture(() -> pane.getChildren().add(node))`<br>`setupFixture(() -> new Label("label"))` | Creates or modifies JavaFX fixtures within a `Runnable` or `Callable<T>` and waits until the call finished.
`showStage()` | Shows the registered `Stage` and brings it to the front.
`hideStage()` | Hides the registered `Stage`.

Static methods that provide the **internal context**.

Examples | Description
-------- | -----------
`toolkitContext().getRegisteredStage()` | Returns the registered `Stage`.
`toolkitContext().setLaunchTimeoutInMillis(2500)` | Sets the timeout for the launch in milliseconds.
`toolkitContext().setSetupTimeoutInMillis(2500)` | Sets the timeout for the setup in milliseconds.
