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
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.WriteRobot;
import org.loadui.testfx.utils.KeyCodeUtils;

public class WriteRobotImpl implements WriteRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long SLEEP_AFTER_CHARACTER_IN_MILLIS = 25;

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public TypeRobot typeRobot;
    public SleepRobot sleepRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public WriteRobotImpl(TypeRobot typeRobot,
                          SleepRobot sleepRobot) {
        this.typeRobot = typeRobot;
        this.sleepRobot = sleepRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void write(char character) {
        pushCharacter(character);
    }

    public void write(String text) {
        for (char character : Lists.charactersOf(text)) {
            pushCharacter(character);
            sleepRobot.sleep(SLEEP_AFTER_CHARACTER_IN_MILLIS);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void pushCharacter(char character) {
        typeRobot.push(toKeyCodeArray(buildCharacterCombination(character)));
    }

    private List<KeyCode> buildCharacterCombination(char character) {
        if (isNotUpperCase(character)) {
            return buildLowerCaseCombination(character);
        }
        else {
            return buildUpperCaseCombination(character);
        }
    }

    private boolean isNotUpperCase(char character) {
        return !Character.isUpperCase(character);
    }

    private List<KeyCode> buildLowerCaseCombination(char character) {
        return Lists.newArrayList(findKeyCode(character));
    }

    private List<KeyCode> buildUpperCaseCombination(char character) {
        return Lists.newArrayList(KeyCode.SHIFT, findKeyCode(character));
    }

    private KeyCode findKeyCode(char character) {
        return KeyCodeUtils.findKeyCode(character);
    }

    private KeyCode[] toKeyCodeArray(List<KeyCode> keyCodes) {
        return keyCodes.toArray(new KeyCode[keyCodes.size()]);
    }

}
