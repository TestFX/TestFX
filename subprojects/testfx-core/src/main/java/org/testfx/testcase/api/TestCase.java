package org.testfx.testcase.api;

import javafx.stage.Stage;

/**
 * This interface generally needs to be provided by a test case. The adaption to
 * a TestFramework (e.g. JUnit) needs to ensure that these methods are called
 * according to their specification as defined below.
 * <p>
 * Additionally each implementation is required to have the following static
 * initialization and destruction functions:
 * <ul>
 * <li>{@code public static void beforeAll() throws Throwable} - This method
 * will be called only once before any of the tests defined in this class are
 * executed.</li>
 * <li>{@code public static void afterAll() throws Throwable} - This method will
 * be called only once after all of the tests defined in this class are
 * executed.</li>
 * </ul>
 * If the class extends another TestCase the class itself (not the
 * TestFramework) shall call the static initializers/destructors of the
 * superclass.
 */
public interface TestCase {

    /**
     * This method will be called before each test is executed.
     */
    public void beforeTest() throws Throwable;

    /**
     * This method will be called after each test is executed.
     */
    public void afterTest() throws Throwable;

    /**
     * Gets the stage of the test
     * 
     * @return the stage of the test
     */
    public Stage getTestStage();

}
