/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.util.WaitForAsyncUtils;

@Unstable
public class MouseRobotImpl implements MouseRobot {

    public BaseRobot baseRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();

    @Override
    public final Set<MouseButton> getPressedButtons() {
        return Collections.unmodifiableSet(pressedButtons);
    }

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MouseRobotImpl(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(MouseButton... buttons) {
        pressNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void pressNoWait(MouseButton... buttons) {
        if (isArrayEmpty(buttons)) {
            pressPrimaryButton();
        }
        else {
            pressButtons(Arrays.asList(buttons));
        }
    }

    @Override
    public void release(MouseButton... buttons) {
        releaseNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void releaseNoWait(MouseButton... buttons) {
        if (isArrayEmpty(buttons)) {
            releasePressedButtons();
        }
        else {
            releaseButtons(Arrays.asList(buttons));
        }
    }

    @Override
    public void move(Point2D location) {
        moveNoWait(location);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void moveNoWait(Point2D location) {
        moveCursor(location);
    }

    @Override
    public void scroll(int wheelAmount) {
        scrollNoWait(wheelAmount);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void scrollNoWait(int wheelAmount) {
        scrollWheel(wheelAmount);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isArrayEmpty(Object[] elements) {
        return elements.length == 0;
    }

    private void pressPrimaryButton() {
        pressButton(MouseButton.PRIMARY);
    }

    private void releasePressedButtons() {
        releaseButtons(new ArrayList<>(pressedButtons));
    }

    private void pressButtons(Collection<MouseButton> buttons) {
        buttons.forEach(this::pressButton);
    }

    private void releaseButtons(Collection<MouseButton> buttons) {
        buttons.forEach(this::releaseButton);
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

    private void moveCursor(Point2D location) {
        baseRobot.moveMouse(location);
    }

    private void scrollWheel(int amount) {
        baseRobot.scrollMouse(amount);
    }

}
