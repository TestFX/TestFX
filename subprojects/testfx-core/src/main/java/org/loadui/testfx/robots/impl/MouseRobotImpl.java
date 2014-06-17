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
package org.loadui.testfx.robots.impl;

import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseButton;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.ScreenRobot;

public class MouseRobotImpl implements MouseRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobot screenRobot;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Set<MouseButton> pressedButtons = Sets.newHashSet();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MouseRobotImpl(ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(MouseButton... buttons) {
        if (isEmptyButtonList(buttons)) {
            pressPrimaryButton();
        }
        else {
            pressButtons(Lists.newArrayList(buttons));
        }
    }

    @Override
    public void release(MouseButton... buttons) {
        if (isEmptyButtonList(buttons)) {
            releasePressedButtons();
        }
        else {
            releaseButtons(Lists.newArrayList(buttons));
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isEmptyButtonList(MouseButton... buttons) {
        return buttons.length == 0;
    }

    private void pressPrimaryButton() {
        pressButton(MouseButton.PRIMARY);
    }

    private void releasePressedButtons() {
        releaseButtons(Lists.newArrayList(pressedButtons));
    }

    private void pressButtons(List<MouseButton> buttons) {
        for (MouseButton button : buttons) {
            pressButton(button);
        }
    }

    private void releaseButtons(List<MouseButton> buttons) {
        for (MouseButton button : buttons) {
            releaseButton(button);
        }
    }

    private void pressButton(MouseButton button) {
        if (pressedButtons.add(button)) {
            screenRobot.pressMouse(button);
        }
    }

    private void releaseButton(MouseButton button) {
        if (this.pressedButtons.remove(button)) {
            this.screenRobot.releaseMouse(button);
        }
    }

}
