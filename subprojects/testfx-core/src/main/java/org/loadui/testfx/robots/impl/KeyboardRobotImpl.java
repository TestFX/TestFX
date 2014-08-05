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
import javafx.scene.input.KeyCode;

import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.ScreenRobot;

public class KeyboardRobotImpl implements KeyboardRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobot screenRobot;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Set<KeyCode> pressedKeys = Sets.newHashSet();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public KeyboardRobotImpl(ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(KeyCode... keyCodes) {
        pressKeys(Lists.newArrayList(keyCodes));
    }

    @Override
    public void release(KeyCode... keyCodes) {
        if (isArrayEmpty(keyCodes)) {
            releasePressedKeys();
        }
        else {
            releaseKeys(Lists.newArrayList(keyCodes));
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isArrayEmpty(Object[] elements) {
        return elements.length == 0;
    }

    private void releasePressedKeys() {
        releaseKeys(Lists.newArrayList(pressedKeys));
    }

    private void pressKeys(List<KeyCode> keyCodes) {
      keyCodes.forEach(this::pressKey);
    }

    private void releaseKeys(List<KeyCode> keyCodes) {
      keyCodes.forEach(this::releaseKey);
    }

    private void pressKey(KeyCode keyCode) {
        if (pressedKeys.add(keyCode)) {
            screenRobot.pressKey(keyCode);
        }
    }

    private void releaseKey(KeyCode keyCode) {
        if (pressedKeys.remove(keyCode)) {
            screenRobot.releaseKey(keyCode);
        }
    }

}
