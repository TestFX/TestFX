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
import javafx.scene.input.KeyCode;

import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.ScreenRobot;

public class KeyboardRobotImpl implements KeyboardRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ScreenRobot screenRobot;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public KeyboardRobotImpl(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(KeyCode... keyCodes) {
        for (KeyCode keyCode : keyCodes) {
            if (pressedKeys.add(keyCode)) {
                screenRobot.pressKey(keyCode);
            }
        }
    }

    @Override
    public void release(KeyCode... keyCodes) {
        if (keyCodes.length == 0) {
            for (KeyCode button : pressedKeys) {
                screenRobot.releaseKey(button);
            }
            pressedKeys.clear();
        }
        else {
            for (KeyCode keyCode : keyCodes) {
                if (pressedKeys.remove(keyCode)) {
                    screenRobot.releaseKey(keyCode);
                }
            }
        }
    }

}
