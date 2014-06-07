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

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseButton;

import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.ScreenRobot;

public class MouseRobotImpl implements MouseRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ScreenRobot screenRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MouseRobotImpl(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(MouseButton... mouseButtons) {
        if (mouseButtons.length == 0) {
            press(MouseButton.PRIMARY);
        }

        for (MouseButton mouseButton : mouseButtons) {
            if (pressedButtons.add(mouseButton)) {
                screenRobot.pressMouse(mouseButton);
            }
        }
    }

    @Override
    public void release(MouseButton... mouseButtons) {
        if (mouseButtons.length == 0) {
            for (MouseButton mouseButton : pressedButtons) {
                screenRobot.releaseMouse(mouseButton);
            }
            pressedButtons.clear();
        }
        else {
            for (MouseButton button : mouseButtons) {
                if (pressedButtons.remove(button)) {
                    screenRobot.releaseMouse(button);
                }
            }
        }
    }

}
