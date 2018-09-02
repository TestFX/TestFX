/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.robot.impl;

import java.util.Objects;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.testfx.robot.ClickRobot;
import org.testfx.robot.Motion;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.WaitForInputEvent;

public class ClickRobotImpl implements ClickRobot {

    static final long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEFAULT = 50;
    static final long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_AGGRESSIVE = 0;
    static final long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEBUG = 100;
    static long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEFAULT;
    static final long CLICK_TO_DEFAULT = 500;
    static final long CLICK_TO_AGGRESSIVE = 100;
    static final long CLICK_TO_DEBUG = 1000;
    static long CLICK_TO = CLICK_TO_DEFAULT;
    static boolean verify = true;

    private final MouseRobot mouseRobot;
    private final MoveRobot moveRobot;
    private final SleepRobot sleepRobot;
    
    

    /**
     * Sets all timing relevant values to the defined default values
     */
    public static void setDefaultTiming() {
        SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEFAULT;
        CLICK_TO = CLICK_TO_DEFAULT;
    }
    /**
     * Sets all timing relevant values to be very fast. Timing may not be guaranteed in all cases,
     * violations may occur. This setup shouldn't generally be used. It is mainly used for testing. 
     */
    public static void setAggressiveTiming() {
        SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_AGGRESSIVE;
        CLICK_TO = CLICK_TO_AGGRESSIVE;
    }
    /**
     * Sets all timing relevant values to a value, that allows the user to follow the test
     * on screen for debugging. This option may also be used to identify timing issues in 
     * a test
     */
    public static void setDebugTiming() {
        SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEBUG;
        CLICK_TO = CLICK_TO_DEBUG;
    }

    public ClickRobotImpl(MouseRobot mouseRobot, MoveRobot moveRobot, SleepRobot sleepRobot) {
        Objects.requireNonNull(mouseRobot, "mouseRobot must not be null");
        Objects.requireNonNull(moveRobot, "moveRobot must not be null");
        Objects.requireNonNull(sleepRobot, "sleepRobot must not be null");
        this.mouseRobot = mouseRobot;
        this.moveRobot = moveRobot;
        this.sleepRobot = sleepRobot;
    }

    @Override
    public void clickOn(MouseButton... buttons) {
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofList(CLICK_TO, l -> l.stream().filter(e -> e instanceof MouseEvent && 
                ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_CLICKED)).count() >= buttons.length &&
                l.stream().filter(e -> e instanceof MouseEvent && 
                ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_RELEASED)).count() >= buttons.length, 
                true);
        }
        mouseRobot.pressNoWait(buttons);
        mouseRobot.releaseNoWait(buttons);
        if (verify) {
            try {
                w.waitFor();
            }
            catch (Exception e) {
                System.err.println("Waiting for mouse click failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
                //TODO#615 remove debugging only
                e.printStackTrace();
            }
        }
        WaitForAsyncUtils.waitForFxEvents();
        
    }

    @Override
    public void clickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        moveRobot.moveTo(pointQuery, motion);
        clickOn(buttons);
    }

    @Override
    public void doubleClickOn(MouseButton... buttons) {
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofList(CLICK_TO, 
                l -> l.stream().filter(e -> e instanceof MouseEvent && ((MouseEvent)e).getEventType()
                    .equals(MouseEvent.MOUSE_CLICKED) && 
                    ((MouseEvent)e).getClickCount() >= 2).count() >= buttons.length &&
                l.stream().filter(e -> e instanceof MouseEvent && 
                    ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_RELEASED)).count() >= 
                    2 * buttons.length, true);
        }
        mouseRobot.pressNoWait(buttons);
        mouseRobot.releaseNoWait(buttons);
        mouseRobot.pressNoWait(buttons);
        mouseRobot.releaseNoWait(buttons);

        if (verify) {
            try {
                w.waitFor();
            }
            catch (Exception e) {
                System.err.println("Waiting for mouse double click failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
            }
        }
        WaitForAsyncUtils.waitForFxEvents();
        sleepRobot.sleep(SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS);
    }

    @Override
    public void doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        moveRobot.moveTo(pointQuery, motion);
        doubleClickOn(buttons);
    }

}
