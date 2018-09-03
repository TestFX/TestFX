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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.util.TestFxTimeoutException;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.WaitForInputEvent;

public class MouseRobotImpl implements MouseRobot {

    private final BaseRobot baseRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    static final long MOUSE_TO_DEFAULT = 500;
    static final long MOUSE_TO_AGGRESSIVE = 100;
    static final long MOUSE_TO_DEBUG = 1000;
    static long MOUSE_TO = MOUSE_TO_DEFAULT;
    static boolean verify = true;

    /**
     * Sets all timing relevant values to the defined default values
     */
    public static void setDefaultTiming() {
        MOUSE_TO = MOUSE_TO_DEFAULT;
    }
    /**
     * Sets all timing relevant values to be very fast. Timing may not be guaranteed in all cases,
     * violations may occur. This setup shouldn't generally be used. It is mainly used for testing. 
     */
    public static void setAggressiveTiming() {
        MOUSE_TO = MOUSE_TO_AGGRESSIVE;
    }
    /**
     * Sets all timing relevant values to a value, that allows the user to follow the test
     * on screen for debugging. This option may also be used to identify timing issues in 
     * a test
     */
    public static void setDebugTiming() {
        MOUSE_TO = MOUSE_TO_DEBUG;
    }
    
    public MouseRobotImpl(BaseRobot baseRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        this.baseRobot = baseRobot;
    }
 
    @Override
    public void press(MouseButton... buttons) {
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofStream(MOUSE_TO, s -> s.filter(e -> e instanceof MouseEvent && 
                ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_PRESSED)).count() >= buttons.length, true);
        }
        pressNoWait(buttons);
        if (verify) {
            try {
                w.waitFor();
            }
            catch (TestFxTimeoutException e) {
                System.err.println("Waiting for mouse failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
            }
        }
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void pressNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            pressButton(MouseButton.PRIMARY);
        }
        else {
            Arrays.asList(buttons).forEach(this::pressButton);
        }
    }

    @Override
    public void release(MouseButton... buttons) {
        if ((buttons == null || buttons.length == 0) && pressedButtons.size() == 0) {
            return;
        }
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofStream(MOUSE_TO, s -> s.filter(e -> e instanceof MouseEvent && 
                ((MouseEvent)e).getEventType().equals(MouseEvent.MOUSE_RELEASED)).count() >= 
                ((buttons == null || buttons.length == 0) ? pressedButtons.size() : buttons.length), true);
        }
        releaseNoWait(buttons);
        if (verify) {
            try {
                w.waitFor();
            }
            catch (TestFxTimeoutException e) {
                System.err.println("Waiting for mouse failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
            }
        }
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void releaseNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            new ArrayList<>(pressedButtons).forEach(this::releaseButton);
        }
        else {
            Arrays.asList(buttons).forEach(this::releaseButton);
        }
    }

    @Override
    public void move(Point2D location) {
        moveNoWait(location);
        if (verify) {
            try {
                WaitForAsyncUtils.waitForFxCondition(MOUSE_TO, TimeUnit.MILLISECONDS, () -> {
                    Point2D loc = baseRobot.retrieveMouse();
                    // account for rounding in robot...
                    //System.out.println("To " + location + " Current " + loc);
                    return loc.getX() <= location.getX() + 1.0 && loc.getX() >= location.getX() - 1.0 &&
                            loc.getY() <= location.getY() + 1.0 && loc.getY() >= location.getY() - 1.0;
                });
            }
            catch (Exception e) {
                System.err.println("Waiting for mouse failed. Timing may be corrupted in this test.");
                System.err.println("The event may have occured outside of the test application!");
            }
        }
        // not guaranteed to have arrived at JavaFx Event Queue...
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void moveNoWait(Point2D location) {
        baseRobot.moveMouse(location);
    }

    @Override
    public void scroll(int wheelAmount) {
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofEvent(MOUSE_TO, e -> e instanceof ScrollEvent, true);
        }
        scrollNoWait(wheelAmount);
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
    public void scrollNoWait(int wheelAmount) {
        baseRobot.scrollMouse(wheelAmount);
    }

    @Override
    public final Set<MouseButton> getPressedButtons() {
        return Collections.unmodifiableSet(pressedButtons);
    }

    private void pressButton(MouseButton button) {
        if (pressedButtons.add(button)) {
            baseRobot.pressMouse(button);
        }
    }

    private void releaseButton(MouseButton button) {
        if (pressedButtons.remove(button)) {
            baseRobot.releaseMouse(button);
        }
    }

}
