package org.testfx.testcase.api;

import javafx.application.Application;
import javafx.stage.Stage;

import org.testfx.api.FxToolkit;

/**
 * This is the base class to test JavaFx Applications. The base class provides
 * any required initialization and will launch and show the Application returned
 * by {@code createApplication()} on the screen. The component under test is
 * accessible via {@code getApplication()}.
 *
 * @param <T> the type of the application under test
 * @see TestCase
 */
public abstract class ApplicationClassTestBase<T extends Application> extends TestCaseBase {

    private T application;
    private Stage testStage;

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
        testStage = FxToolkit.registerPrimaryStage();
        application = FxToolkit.setupApplication(() -> createApplication(), getArguments());
        interact(() -> testStage.toFront());
        moveTo(getTestStage());
    }

    @Override
    public void afterTest() throws Throwable {
        super.afterTest();
        FxToolkit.cleanupApplication(application);
    }

    /**
     * Creates a instance of the application under test for use in the next test and
     * returns it.<br> 
     * This function may be called on the Fx-Application-Thread, so do
     * not use any methods that are waiting for Fx-Events (e.g. Robot functions).
     * 
     * @return a instance of the application under test
     */
    public abstract T createApplication();

    /**
     * Gets the arguments to launch the application with for the current test.
     * 
     * @return the arguments for the application
     */
    public abstract String[] getArguments();

    /**
     * Gets the application under test for this test. The application under test
     * will only be available after initialization.
     * 
     * @return the application under test
     */
    public T getApplication() {
        return application;
    }

    @Override
    public Stage getTestStage() {
        return testStage;
    }

}
