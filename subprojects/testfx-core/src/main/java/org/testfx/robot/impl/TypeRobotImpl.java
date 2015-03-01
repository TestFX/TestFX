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
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.testfx.api.annotation.Unstable;
import org.testfx.robot.KeyboardRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.TypeRobot;

@Unstable
public class TypeRobotImpl implements TypeRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long SLEEP_AFTER_KEY_CODE_IN_MILLIS = 25;

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
    public void push(KeyCode... combination) {
        pushKeyCodeCombination(combination);
    }

    @Override
    public void push(KeyCodeCombination combination) {
        pushKeyCodeCombination(combination);
    }

    @Override
    public void type(KeyCode... keys) {
        for (KeyCode keyCode : keys) {
            pushKeyCode(keyCode);
            sleepRobot.sleep(SLEEP_AFTER_KEY_CODE_IN_MILLIS);
        }
    }

    @Override
    public void type(KeyCode key,
                     int times) {
        for (int index = 0; index < times; index++) {
            pushKeyCode(key);
            sleepRobot.sleep(SLEEP_AFTER_KEY_CODE_IN_MILLIS);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void pushKeyCode(KeyCode keyCode) {
        keyboardRobot.pressNoWait(keyCode);
        keyboardRobot.release(keyCode);
    }

    public void pushKeyCodeCombination(KeyCode... keyCodeCombination) {
        List<KeyCode> keyCodesForwards = Lists.newArrayList(keyCodeCombination);
        List<KeyCode> keyCodesBackwards = Lists.reverse(keyCodesForwards);
        keyboardRobot.pressNoWait(toKeyCodeArray(keyCodesForwards));
        keyboardRobot.release(toKeyCodeArray(keyCodesBackwards));
    }

    public void pushKeyCodeCombination(KeyCodeCombination keyCodeCombination) {
        List<KeyCode> keyCodes = filterKeyCodes(keyCodeCombination);
        pushKeyCodeCombination(toKeyCodeArray(keyCodes));
    }

    private List<KeyCode> filterKeyCodes(KeyCodeCombination keyCombination) {
        Map<KeyCombination.Modifier, KeyCombination.ModifierValue> modifiers = ImmutableMap.of(
            KeyCombination.SHIFT_DOWN, keyCombination.getShift(),
            KeyCombination.CONTROL_DOWN, keyCombination.getControl(),
            KeyCombination.ALT_DOWN, keyCombination.getAlt(),
            KeyCombination.META_DOWN, keyCombination.getMeta(),
            KeyCombination.SHORTCUT_DOWN, keyCombination.getShortcut()
        );
        List<KeyCode> modifierKeyCodes = FluentIterable.from(modifiers.entrySet())
            .filter(entry -> entry.getKey().getValue() == entry.getValue())
            .transform(entry -> entry.getKey().getKey())
            .toList();
        return ImmutableList.<KeyCode>builder()
            .addAll(modifierKeyCodes)
            .add(keyCombination.getCode())
            .build();
    }

    private KeyCode[] toKeyCodeArray(List<KeyCode> keyCodes) {
        return keyCodes.toArray(new KeyCode[keyCodes.size()]);
    }

}
