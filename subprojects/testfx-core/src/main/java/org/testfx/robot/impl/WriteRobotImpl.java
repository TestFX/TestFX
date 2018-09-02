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
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.WriteRobot;
import org.testfx.service.finder.WindowFinder;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.WaitForInputEvent;

public class WriteRobotImpl implements WriteRobot {

    static int SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT = 25;
    static final int SLEEP_AFTER_CHARACTER_IN_MILLIS_AGGRESSIVE = 0;
    static final int SLEEP_AFTER_CHARACTER_IN_MILLIS_DEBUG = 50;
    static int SLEEP_AFTER_CHARACTER_IN_MILLIS = SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT;

    static final int CHAR_TO_DEFAULT = 500;
    static int CHAR_TO = CHAR_TO_DEFAULT;
    static boolean verify = true;
    static boolean debug;

    static {
        int writeSleep;
        try {
            writeSleep = Integer.getInteger("testfx.robot.write_sleep", SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT);
        }
        catch (NumberFormatException e) {
            System.err.println("\"testfx.robot.write_sleep\" property must be a number but was: \"" +
                    System.getProperty("testfx.robot.write_sleep") + "\".\nUsing default of \"" + 
                    SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT + "\" milliseconds.");
            e.printStackTrace();
            writeSleep = 25;
        }
        SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT = writeSleep;
        SLEEP_AFTER_CHARACTER_IN_MILLIS = writeSleep;
    }

    /**
     * Sets all timing relevant values to the defined default values
     */
    public static void setDefaultTiming() {
        SLEEP_AFTER_CHARACTER_IN_MILLIS = SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT;
    }
    /**
     * Sets all timing relevant values to be very fast. Timing may not be guaranteed in all cases,
     * violations may occur. This setup shouldn't generally be used. It is mainly used for testing. 
     */
    public static void setAggressiveTiming() {
        SLEEP_AFTER_CHARACTER_IN_MILLIS = SLEEP_AFTER_CHARACTER_IN_MILLIS_AGGRESSIVE;
    }
    /**
     * Sets all timing relevant values to a value, that allows the user to follow the test
     * on screen for debugging. This option may also be used to identify timing issues in 
     * a test
     */
    public static void setDebugTiming() {
        SLEEP_AFTER_CHARACTER_IN_MILLIS = SLEEP_AFTER_CHARACTER_IN_MILLIS_DEBUG;
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
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofList(CHAR_TO,
                l -> l.stream().filter(e -> e instanceof KeyEvent && 
                    ((KeyEvent) e).getEventType().equals(KeyEvent.KEY_TYPED)).count() >= 1 &&
                l.stream().filter(e -> e instanceof KeyEvent && 
                    ((KeyEvent) e).getEventType().equals(KeyEvent.KEY_RELEASED)).count() >= 1, true);
        }
        Scene scene = fetchTargetWindow().getScene();
        typeCharacterInScene(character, scene);
        if (verify) {
            try {
                w.waitFor();
            } 
            catch (Exception e) {
                System.err.println("Waiting for writing keys failed. Timing may be corrupted in this test.");
                System.err.println("The key may have been typed outside of the test application!");
            }
        }
    }

    @Override
    public void write(String text) {
        write(text, SLEEP_AFTER_CHARACTER_IN_MILLIS);
    }

    @Override
    public void write(String text, int sleepMillis) {
        Scene scene = fetchTargetWindow().getScene();
        WaitForInputEvent w = null;
        if (verify) {
            w = WaitForInputEvent.ofStream(text.length() * CHAR_TO,
                s -> s.filter(e -> e instanceof KeyEvent && ((KeyEvent) e).getEventType().equals(KeyEvent.KEY_TYPED))
                .count() >= text.length(), true);
        }
        for (char character : text.chars().mapToObj(i -> (char) i).collect(Collectors.toList())) {
            typeCharacterInScene(character, scene);
            if (sleepMillis > 0) {
                sleepRobot.sleep(sleepMillis);
            }
        }
        if (verify) {
            try {
                w.waitFor();
            } 
            catch (Exception e) {
                System.err.println("Waiting for writing keys failed. Timing may be corrupted in this test.");
                System.err.println("The key may have been typed outside of the test application!");
            }
        }
        WaitForAsyncUtils.waitForFxEvents();
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
