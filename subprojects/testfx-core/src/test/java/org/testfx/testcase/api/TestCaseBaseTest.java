package org.testfx.testcase.api;

import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.Assert.fail;

/**
 * Basically just tests initialization and cleanup
 */
public class TestCaseBaseTest {

    // to verify cleanup of Mouse and Keyboard, we need some graphic components, as
    // the
    // currently pressed key and buttons returned by robots might be incorrect (new
    // Robot...)
    // --> Functionality is tested in ComponentTestBaseTest

    // fail on exception before
    @Test
    public void failOnExceptionBeforeTest() throws Throwable {
        // given
        // ensure initialized
        TestCase test = new TestCase();
        TestCaseBase.beforeAll();
        test.beforeTest();
        test.afterTest();
        TestCase.afterAll();

        // when
        // simulate a test that had some exceptions
        // turn off autoExceptions, otherwise we may not have multiple exceptions in the queue
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.printException = false;
        WaitForAsyncUtils.async(() -> {
            throw new Exception("FirstException");
        });
        WaitForAsyncUtils.async(() -> {
            throw new Exception("SecondException");
        });
        WaitForAsyncUtils.async(() -> {
            throw new Exception("ThirdException");
        });

        // then
        try {
            test = new TestCase();
            TestCaseBase.beforeAll();
            test.beforeTest();
        } 
        catch (Exception e) {
            try {
                WaitForAsyncUtils.checkException();
            } 
            catch (Exception ex) {
                fail("There was still an Exception on the stack");
            }
            return;
        } 
        finally {
            WaitForAsyncUtils.printException = true;
            WaitForAsyncUtils.autoCheckException = true;
        }
        fail("Expected an exception");
    }

    @Test
    public void failOnExceptionInTest() throws Throwable {
        // given
        TestCase test = new TestCase();
        TestCaseBase.beforeAll();
        test.beforeTest();

        // when
        // simulate a test that has some exceptions
        // turn off autoExceptions, otherwise we may not have multiple exceptions in the queue
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.printException = false;
        WaitForAsyncUtils.async(() -> {
            throw new Exception("FirstException");
        });
        WaitForAsyncUtils.async(() -> {
            throw new Exception("SecondException");
        });
        WaitForAsyncUtils.async(() -> {
            throw new Exception("ThirdException");
        });

        // then
        try {
            test = new TestCase();
            test.afterTest();
            TestCaseBase.afterAll();
        } 
        catch (Exception e) {
            try {
                WaitForAsyncUtils.checkException();
            } 
            catch (Exception ex) {
                fail("There was still an Exception on the stack");
            }
            return;
        } 
        finally {
            WaitForAsyncUtils.printException = true;
            WaitForAsyncUtils.autoCheckException = true;
        }
        fail("Expected an exception");
    }

    private static class TestCase extends TestCaseBase {

        @Override
        public Stage getTestStage() {
            return null;
        }

    }
}
