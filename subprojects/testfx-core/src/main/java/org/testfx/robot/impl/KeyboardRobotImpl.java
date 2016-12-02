/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.robot.impl;

import java.util.List;
import java.util.Set;
import javafx.scene.input.KeyCode;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.KeyboardRobot;

@Unstable
public class KeyboardRobotImpl implements KeyboardRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Set<KeyCode> pressedKeys = Sets.newHashSet();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public KeyboardRobotImpl(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(KeyCode... keys) {
        pressNoWait(keys);
        baseRobot.awaitEvents();
    }

    @Override
    public void pressNoWait(KeyCode... keys) {
        pressKeys(Lists.newArrayList(keys));
    }

    @Override
    public void release(KeyCode... keys) {
        releaseNoWait(keys);
        baseRobot.awaitEvents();
    }

    @Override
    public void releaseNoWait(KeyCode... keys) {
        if (isArrayEmpty(keys)) {
            releasePressedKeys();
        }
        else {
            releaseKeys(Lists.newArrayList(keys));
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isArrayEmpty(Object[] elements) {
        return elements.length == 0;
    }

    private void pressKeys(List<KeyCode> keyCodes) {
        keyCodes.forEach(this::pressKey);
    }

    private void releaseKeys(List<KeyCode> keyCodes) {
        keyCodes.forEach(this::releaseKey);
    }

    private void releasePressedKeys() {
        releaseKeys(Lists.newArrayList(pressedKeys));
    }

    private void pressKey(KeyCode keyCode) {
        if (pressedKeys.add(keyCode)) {
            baseRobot.pressKeyboard(keyCode);
        }
    }

    private void releaseKey(KeyCode keyCode) {
        if (pressedKeys.remove(keyCode)) {
            baseRobot.releaseKeyboard(keyCode);
        }
    }

}
