package org.testfx.testcase.api;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.testfx.api.FxToolkit;

/**
 * This is the base class to test stages. The base class provides any required
 * initialization and will show the component returned by {@code createStage} on
 * the screen. The stage under test is accessible via {@code getTestStage()}.
 *
 * @param <T> the type of the stage under test
 * @see TestCase
 */
public abstract class StageTestBase<T extends Stage> extends TestCaseBase {

    T testStage;

    @Override
    public T getTestStage() {
        return testStage;
    }

    /**
     * Creates a instance of the stage under test for use in the next test and
     * returns it.
     * 
     * @return a instance of the stage under test
     */
    public abstract T createStage();

    @Override
    public void beforeTest() throws Throwable {
        super.beforeTest();
        FxToolkit.registerStage(() -> {
            testStage = createStage();
            testStage.initStyle(StageStyle.UNDECORATED);
            testStage.show();
            testStage.toFront();
            moveTo(getTestStage());
            return testStage;
        });
    }

    @Override
    public void afterTest() throws Throwable {
        testStage = null;
        super.afterTest();
    }

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

}
