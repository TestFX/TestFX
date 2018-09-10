package org.testfx.util;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WaitForInputEventTest extends InternalTestCaseBase {

    @Test
    public void waitForEventTest() {
        // given
        moveTo(getTestStage());
        long t = System.currentTimeMillis();

        // when
        WaitForInputEvent w = WaitForInputEvent.ofEvent(1000, e -> e instanceof MouseEvent, true);
        clickOn(MouseButton.PRIMARY);
        w.waitFor();

        // then
        assertTrue(System.currentTimeMillis() - t < 900);
    }

    @Test
    public void waitForEventStreamTest() {
        // given
        moveTo(getTestStage());
        long t = System.currentTimeMillis();

        // when
        WaitForInputEvent w = WaitForInputEvent.ofStream(1000, s -> s.filter(e -> e instanceof MouseEvent).count() >= 2,
                true);
        clickOn(MouseButton.PRIMARY);
        w.waitFor();

        // then
        assertTrue(System.currentTimeMillis() - t < 900);
    }

    @Test
    public void waitForEventExceptionTest() {
        // given
        moveTo(getTestStage());

        // when
        WaitForInputEvent w = WaitForInputEvent.ofEvent(1000, e -> e instanceof MouseEvent, true);
        try {
            w.waitFor();
            // then
            fail("Waiting for events didn't timeout");
        } 
        catch (TestFxTimeoutException e) {
            if (e.getMessage().indexOf("Waiting for InputEvents timed out") == -1) {
                throw e;
            }
        }

    }

    @Test
    public void waitForEventNoExceptionTest() {
        // given
        moveTo(getTestStage());
        long t = System.currentTimeMillis();

        // when
        WaitForInputEvent w = WaitForInputEvent.ofEvent(1000, e -> e instanceof MouseEvent, false);
        w.waitFor();

        // then
        assertTrue("Timeout didn't wait long enough", System.currentTimeMillis() - t > 1000);
    }

}
