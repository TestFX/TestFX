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

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.input.KeyCode;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.KeyboardRobot;
import org.testfx.util.WaitForAsyncUtils;

public class KeyboardRobotImpl implements KeyboardRobot {

    protected static final KeyCode OS_SPECIFIC_SHORTCUT = System.getProperty("os.name").toLowerCase(Locale.US)
            .startsWith("mac") ? KeyCode.COMMAND : KeyCode.CONTROL;

    private final BaseRobot baseRobot;
    private final Set<KeyCode> pressedKeys = ConcurrentHashMap.newKeySet();

    public KeyboardRobotImpl(BaseRobot baseRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        this.baseRobot = baseRobot;
    }

    @Override
    public void press(KeyCode... keys) {
        pressNoWait(keys);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void pressNoWait(KeyCode... keys) {
        Arrays.asList(keys).forEach(this::pressKey);
    }

    @Override
    public void release(KeyCode... keys) {
        releaseNoWait(keys);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void releaseNoWait(KeyCode... keys) {
        if (keys.length == 0) {
            pressedKeys.forEach(this::releaseKey);
        }
        else {
            Arrays.asList(keys).forEach(this::releaseKey);
        }
    }

    @Override
    public final Set<KeyCode> getPressedKeys() {
        return Collections.unmodifiableSet(pressedKeys);
    }

    private void pressKey(KeyCode keyCode) {
        KeyCode realKeyCode = keyCode == KeyCode.SHORTCUT ? OS_SPECIFIC_SHORTCUT : keyCode;
        if (pressedKeys.add(realKeyCode)) {
            baseRobot.pressKeyboard(realKeyCode);
        }
    }

    private void releaseKey(KeyCode keyCode) {
        KeyCode realKeyCode = keyCode == KeyCode.SHORTCUT ? OS_SPECIFIC_SHORTCUT : keyCode;
        if (pressedKeys.remove(realKeyCode)) {
            baseRobot.releaseKeyboard(realKeyCode);
        }
    }

}
