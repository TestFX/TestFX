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

import java.util.Objects;
import java.util.stream.Collectors;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.WriteRobot;
import org.testfx.service.finder.WindowFinder;
import org.testfx.util.WaitForAsyncUtils;

public class WriteRobotImpl implements WriteRobot {

    private static final int SLEEP_AFTER_CHARACTER_IN_MILLIS;

    static {
        int writeSleep;
        try {
            writeSleep = Integer.getInteger("testfx.robot.write_sleep", 25);
        }
        catch (NumberFormatException e) {
            System.err.println("\"testfx.robot.write_sleep\" property must be a number but was: \"" +
                    System.getProperty("testfx.robot.write_sleep") + "\".\nUsing default of \"25\" milliseconds.");
            e.printStackTrace();
            writeSleep = 25;
        }
        SLEEP_AFTER_CHARACTER_IN_MILLIS = writeSleep;
    }

    private final BaseRobot baseRobot;
    private final SleepRobot sleepRobot;
    private final WindowFinder windowFinder;

    public WriteRobotImpl(BaseRobot baseRobot, SleepRobot sleepRobot, WindowFinder windowFinder) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        Objects.requireNonNull(sleepRobot, "sleepRobot must not be null");
        Objects.requireNonNull(windowFinder, "windowFinder must not be null");
        this.baseRobot = baseRobot;
        this.sleepRobot = sleepRobot;
        this.windowFinder = windowFinder;
    }

    @Override
    public void write(char character) {
        Scene scene = fetchTargetWindow().getScene();
        typeCharacterInScene(character, scene);
    }

    @Override
    public void write(String text) {
        write(text, SLEEP_AFTER_CHARACTER_IN_MILLIS);
    }

    @Override
    public void write(String text, int sleepMillis) {
        Scene scene = fetchTargetWindow().getScene();
        for (char character : text.chars().mapToObj(i -> (char) i).collect(Collectors.toList())) {
            typeCharacterInScene(character, scene);
            sleepRobot.sleep(sleepMillis);
        }
    }

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
        WaitForAsyncUtils.waitForFxEvents();
    }

    private KeyCode determineKeyCode(char character) {
        KeyCode key = KeyCode.UNDEFINED;
        key = (character == '\n') ? KeyCode.ENTER : key;
        key = (character == '\t') ? KeyCode.TAB : key;
        return key;
    }

}
