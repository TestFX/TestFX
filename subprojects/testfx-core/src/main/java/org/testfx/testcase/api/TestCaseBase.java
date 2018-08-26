package org.testfx.testcase.api;

import java.util.concurrent.TimeUnit;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

/**
 * This class implements the most simple test case. It does only initialize the
 * Toolkit and does the additionally required setup and shutdown for each
 * test.<br>
 * The setup and shutdown includes releasing the pressed keys and mouse buttons
 * and checking if any uncaught exception occurred within this test.<br>
 * The exceptions are also checked before the test is initialized, to inform the
 * user, that one of the previous tests had an exception (and may have
 * succeeded, because he didn't check the exceptions).
 *
 */
public abstract class TestCaseBase extends FxRobot implements TestCase {

    /**
     * The static initializer, that must be called before any test is executed.
     * 
     * @throws Throwable any throwable, that occurred during initialization
     */
    public static void beforeAll() throws Throwable {
        // Used to start JavaFx, if not running
        if (!FxToolkit.isFXApplicationThreadRunning()) {
            FxToolkit.registerPrimaryStage();
        }
        // Verify it is up and running
        if (!FxToolkit.isFXApplicationThreadRunning()) {
            throw new RuntimeException("Failed to initialize Toolkit");
        }
    }

    /**
     * The static clean up, that must be called after all test have been executed.
     * 
     * @throws Throwable any throwable, that occurred during clean up
     */
    public static void afterAll() throws Throwable {

    }

    @Override
    public void beforeTest() throws Throwable {
        // release all keys
        release(new KeyCode[0]);
        // release all mouse buttons
        release(new MouseButton[0]);

        // check that all exceptions were handled by the user test
        try {
            WaitForAsyncUtils.checkException();
        } 
        catch (Throwable e) {
            WaitForAsyncUtils.clearExceptions(); // There might be more exceptions on the stack
            throw new RuntimeException("Exception in a previous test!", e); // make test fail
        }
    }

    @Override
    public void afterTest() throws Throwable {
        // release all keys
        release(new KeyCode[0]);
        // release all mouse buttons
        release(new MouseButton[0]);

        FxToolkit.cleanupStages();

        // check that all exceptions were handled by the user test
        try {
            WaitForAsyncUtils.checkException();
        } 
        catch (Throwable e) {
            WaitForAsyncUtils.clearExceptions(); // There might be more exceptions on the stack
            throw e; // make test fail
        }
    }
    
    /**
     * This method will initialize the given stage. It will provide consistent initial
     * conditions for each test.   It will wait until the stage is visible and place the
     * mouse pointer to the center of the stage.
     * @param s the main stage of the test to initialize
     */
    protected void initStage(Stage s) {
        //there are some flaws with the
        if (s != null) {
            interrupt(1, TimeUnit.SECONDS, () -> s.isShowing());
            interrupt(); // wait for events after stage is showing
            // check size of stage (preventing successive failures...)
            double x = 0;
            try {
                x = WaitForAsyncUtils.asyncFx(() -> {return s.getX();}).get(500, TimeUnit.MILLISECONDS);
            }
            catch(Exception e) {
                throw new RuntimeException("Failed to get stage coordinates",e);
            }
            if (Double.isNaN(x)) {
                throw new RuntimeException("Coordinates of stage are NaN. " + 
                        "Most probable reason for this is, that your stage has no size. " +
                        "Try to set preferred size of your component.");
            }
            moveTo(getTestStage());
        }
    }

}
