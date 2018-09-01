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
    static long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS_DEFAULT;
    static final long CLICK_TO_DEFAULT = 500;
    static long CLICK_TO = CLICK_TO_DEFAULT;
    static boolean verify = true;

    private final MouseRobot mouseRobot;
    private final MoveRobot moveRobot;
    private final SleepRobot sleepRobot;

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
            w = WaitForInputEvent.ofStream(CLICK_TO, s -> s.filter(e -> e instanceof MouseEvent && 
                ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_CLICKED)).count() >= buttons.length &&
                s.filter(e -> e instanceof MouseEvent && 
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
                System.err.println("Waiting for mouse failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
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
            w = WaitForInputEvent.ofStream(CLICK_TO, 
                s -> s.filter(e -> e instanceof MouseEvent && ((MouseEvent)e).getEventType()
                    .equals(MouseEvent.MOUSE_CLICKED) && 
                    ((MouseEvent)e).getClickCount() >= 2).count() >= buttons.length &&
                s.filter(e -> e instanceof MouseEvent && 
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
                System.err.println("Waiting for mouse failed. Timing may be corrupted in this test.");
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
