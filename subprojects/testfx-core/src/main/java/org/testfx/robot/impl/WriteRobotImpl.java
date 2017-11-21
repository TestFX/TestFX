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

import java.util.stream.Collectors;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.WriteRobot;
import org.testfx.service.finder.WindowFinder;

@Unstable
public class WriteRobotImpl implements WriteRobot {

    private static final long SLEEP_AFTER_CHARACTER_IN_MILLIS = 25;

    public BaseRobot baseRobot;
    public SleepRobot sleepRobot;
    public WindowFinder windowFinder;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public WriteRobotImpl(BaseRobot baseRobot,
                          SleepRobot sleepRobot,
                          WindowFinder windowFinder) {
        this.baseRobot = baseRobot;
        this.sleepRobot = sleepRobot;
        this.windowFinder = windowFinder;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void write(char character) {
        Scene scene = fetchTargetWindow().getScene();
        typeCharacterInScene(character, scene);
    }

    @Override
    public void write(String text) {
        Scene scene = fetchTargetWindow().getScene();
        for (char character : text.chars().mapToObj(i -> (char) i).collect(Collectors.toList())) {
            typeCharacterInScene(character, scene);
            sleepRobot.sleep(SLEEP_AFTER_CHARACTER_IN_MILLIS);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Window fetchTargetWindow() {
        Window targetWindow = windowFinder.window(Window::isFocused);
        if (targetWindow == null) {
            targetWindow = windowFinder.targetWindow();
        }
        if (targetWindow == null) {
            targetWindow = windowFinder.window(0);
        }
        return targetWindow;
    }

    private void typeCharacterInScene(char character,
                                      Scene scene) {
        KeyCode key = determineKeyCode(character);
        baseRobot.typeKeyboard(scene, key, Character.toString(character));
        baseRobot.awaitEvents();
    }

    private KeyCode determineKeyCode(char character) {
        KeyCode key = KeyCode.UNDEFINED;
        key = (character == '\n') ? KeyCode.ENTER : key;
        key = (character == '\t') ? KeyCode.TAB : key;
        return key;
    }

}
