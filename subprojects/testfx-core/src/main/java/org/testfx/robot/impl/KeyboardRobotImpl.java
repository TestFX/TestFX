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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.input.KeyCode;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.KeyboardRobot;

@Unstable
public class KeyboardRobotImpl implements KeyboardRobot {

    private static final KeyCode OS_SPECIFIC_SHORTCUT;

    static {
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        OS_SPECIFIC_SHORTCUT = osName.startsWith("mac") ? KeyCode.COMMAND : KeyCode.CONTROL;
    }

    public BaseRobot baseRobot;

    private final Set<KeyCode> pressedKeys = ConcurrentHashMap.newKeySet();

    @Override
    public final Set<KeyCode> getPressedKeys() {
        return Collections.unmodifiableSet(pressedKeys);
    }

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
        pressKeys(Arrays.asList(keys));
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
            releaseKeys(Arrays.asList(keys));
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

    private void releaseKeys(Collection<KeyCode> keyCodes) {
        keyCodes.forEach(this::releaseKey);
    }

    private void releasePressedKeys() {
        releaseKeys(pressedKeys);
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
