package org.testfx.testcase.api;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

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
        if(!FxToolkit.isFXApplicationThreadRunning()) {
            FxToolkit.registerPrimaryStage();
        }
        // Verify it is up and running
        if(!FxToolkit.isFXApplicationThreadRunning()) {
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

}
