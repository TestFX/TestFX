/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2019 The TestFX Contributors
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
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.util.WaitForAsyncUtils;

public class MouseRobotImpl implements MouseRobot {

    /**
     * Duration between atomic mouse action (required for those actions to be correctly detected by OS).
     */
    final static private long mouseActionDurationMillis = 20L;

    private final BaseRobot baseRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    private final SleepRobot sleepRobot;

    public MouseRobotImpl(BaseRobot baseRobot, SleepRobot sleepRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        Objects.requireNonNull(sleepRobot, "sleepRobot must not be null");
        this.baseRobot = baseRobot;
        this.sleepRobot = sleepRobot;
    }
 
    @Override
    public void press(MouseButton... buttons) {
        pressNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void pressNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            pressButton(MouseButton.PRIMARY);
        }
        else {
            Arrays.asList(buttons).forEach(this::pressButton);
        }
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void release(MouseButton... buttons) {
        releaseNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void releaseNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            new ArrayList<>(pressedButtons).forEach(this::releaseButton);
        }
        else {
            Arrays.asList(buttons).forEach(this::releaseButton);
        }
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void move(Point2D location) {
        moveNoWait(location);
        WaitForAsyncUtils.waitForFxEvents();
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void moveNoWait(Point2D location) {
        baseRobot.moveMouse(location);
        sleepRobot.sleep(mouseActionDurationMillis);
    }

    @Override
    public void scroll(int wheelAmount) {
        scrollNoWait(wheelAmount);
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
