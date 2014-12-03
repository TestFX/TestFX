/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.robot.impl;

import java.util.List;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;

public class MouseRobotImpl implements MouseRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Set<MouseButton> pressedButtons = Sets.newHashSet();

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
        baseRobot.awaitEvents();
    }

    @Override
    public void pressNoWait(MouseButton... buttons) {
        if (isArrayEmpty(buttons)) {
            pressPrimaryButton();
        }
        else {
            pressButtons(Lists.newArrayList(buttons));
        }
    }

    @Override
    public void release(MouseButton... buttons) {
        releaseNoWait(buttons);
        baseRobot.awaitEvents();
    }

    @Override
    public void releaseNoWait(MouseButton... buttons) {
        if (isArrayEmpty(buttons)) {
            releasePressedButtons();
        }
        else {
            releaseButtons(Lists.newArrayList(buttons));
        }
    }

    @Override
    public void move(Point2D location) {
        moveNoWait(location);
        baseRobot.awaitEvents();
    }

    @Override
    public void moveNoWait(Point2D location) {
        moveCursor(location);
    }

    @Override
    public void scroll(int wheelAmount) {
        scrollNoWait(wheelAmount);
        baseRobot.awaitEvents();
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
        releaseButtons(Lists.newArrayList(pressedButtons));
    }

    private void pressButtons(List<MouseButton> buttons) {
      buttons.forEach(this::pressButton);
    }

    private void releaseButtons(List<MouseButton> buttons) {
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
