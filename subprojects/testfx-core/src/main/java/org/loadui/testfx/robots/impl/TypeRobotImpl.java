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
import javafx.scene.input.KeyCode;

import com.google.common.collect.Lists;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.utils.KeyCodeUtils;

public class TypeRobotImpl implements TypeRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public KeyboardRobot keyboardRobot;
    public SleepRobot sleepRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public TypeRobotImpl(KeyboardRobot keyboardRobot,
                         SleepRobot sleepRobot) {
        this.keyboardRobot = keyboardRobot;
        this.sleepRobot = sleepRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void write(String text) {
        for (int index = 0; index < text.length(); index++) {
            write(text.charAt(index));
            sleepRobot.sleep(25);
        }
    }

    @Override
    public void write(char character) {
        if (isNotUpperCase(character)) {
            writeLowerCase(character);
        }
        else {
            writeUpperCase(character);
        }
    }

    @Override
    public void push(KeyCode keyCode,
                     int times) {
        for (int index = 0; index < times; index++) {
            pushKey(keyCode);
            sleepRobot.sleep(25);
        }
    }

    @Override
    public void push(KeyCode... keyCodes) {
        pushKeyCombination(keyCodes);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isNotUpperCase(char character) {
        return !Character.isUpperCase(character);
    }

    private void writeLowerCase(char character) {
        pushKey(findKeyCode(character));
    }

    private void writeUpperCase(char character) {
        pushKeyCombination(KeyCode.SHIFT, findKeyCode(character));
    }

    public void pushKey(KeyCode keyCode) {
        keyboardRobot.type(keyCode);
    }

    public void pushKeyCombination(KeyCode... keyCodes) {
        List<KeyCode> keyCodesForwards = Lists.newArrayList(keyCodes);
        List<KeyCode> keyCodesBackwards = Lists.reverse(keyCodesForwards);
        keyboardRobot.press(toKeyCodeArray(keyCodesForwards));
        keyboardRobot.release(toKeyCodeArray(keyCodesBackwards));
    }

    private KeyCode findKeyCode(char character) {
        return KeyCodeUtils.findKeyCode(character);
    }

    private KeyCode[] toKeyCodeArray(List<KeyCode> keyCodes) {
        return keyCodes.toArray(new KeyCode[keyCodes.size()]);
    }

}
