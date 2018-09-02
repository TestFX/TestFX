package org.testfx.testcase.api;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.testfx.api.FxToolkit;

/**
 * This is the base class to test UI components, that extend Node. The base
 * class provides any required initialization and will show the component
 * returned by {@code createComponent()} on the screen. The component under test
 * is accessible via {@code getComponent()}.
 *
 * @param <T> the type of the component under test
 * @see TestCase
 */
public abstract class ComponentTestBase<T extends Node> extends TestCaseBase {

    Stage testStage;
    Scene testScene;
    T component;

    /**
     * Gets the component under test for this test. The component under test will
     * only be available after initialization.
     * 
     * @return the component under test
     */
    public T getComponent() {
        return component;
    }

    @Override
    public Stage getTestStage() {
        return testStage;
    }

    /**
     * Gets the scene of the test. The scene will be available only after
     * initialization.
     * 
     * @return the scene
     */
    public Scene getTestScene() {
        return testScene;
    }

    /**
     * Creates a instance of the component under test for use in the next test and
     * returns it.<br> 
     * This function may be called on the Fx-Application-Thread, so do
     * not use any methods that are waiting for Fx-Events (e.g. Robot functions).
     * 
     * @return a instance of the component under test
     */
    public abstract T createComponent();

    /**
     * The static initializer, that must be called before any test is executed.
     * 
     * @throws Throwable any throwable, that occurred during initialization
     */
    public static void beforeAll() throws Throwable {
        TestCaseBase.beforeAll();
    }

    /**
     * The static clean up, that must be called after all test have been executed.
     * 
     * @throws Throwable any throwable, that occurred during clean up
     */
    public static void afterAll() throws Throwable {
        TestCaseBase.afterAll();
    }

    @Override
    public void beforeTest() throws Throwable {
        super.beforeTest();
        testStage = FxToolkit.registerStage(() -> {
            Stage s = new Stage();
            s.centerOnScreen();
            s.initStyle(StageStyle.UNDECORATED);
            component = createComponent();
            Parent content = null;
            if (component instanceof Parent) {
                content = (Parent) component;
            } else {
                content = new Pane();
                if (component != null) {
                    ((Pane) content).getChildren().add(component);
                }
            }

            testScene = new Scene(content);
            s.setScene(testScene);
            s.show();
            s.toFront();
            return s;
        });
        initStage(getTestStage());
    }

    @Override
    public void afterTest() throws Throwable {
        component = null;
        super.afterTest();
    }

}
