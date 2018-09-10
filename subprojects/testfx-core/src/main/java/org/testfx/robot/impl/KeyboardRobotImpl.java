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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.testfx.internal.PlatformAdapter;
import org.testfx.internal.PlatformAdapter.OS;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.KeyboardRobot;
import org.testfx.util.TestFxTimeoutException;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.WaitForInputEvent;

public class KeyboardRobotImpl implements KeyboardRobot {

    /**
     * This key is sent depending on the platform via the Robot to Java.
     */
    private static final KeyCode OS_SPECIFIC_SHORTCUT = PlatformAdapter.getOs() == OS.mac ? KeyCode.COMMAND :
            KeyCode.CONTROL;

    static final long KEYBOARD_TO_DEFAULT = 500;
    static final long KEYBOARD_TO_AGGRESSIVE = 100;
    static final long KEYBOARD_TO_DEBUG = 1000;
    /**
     * The expected worst case time in ms, between the call to the Robot function and the corresponding event
     * showing up in the JavaFx Event Thread.
     */
    static long KEYBOARD_TO = KEYBOARD_TO_DEFAULT;
    static boolean verify = true;
    
    static boolean debugKeys;
    
    private final BaseRobot baseRobot;
    private final Set<KeyCode> pressedKeys = ConcurrentHashMap.newKeySet();

    

    /**
     * Sets all timing relevant values to the defined default values
     */
    public static void setDefaultTiming() {
        KEYBOARD_TO = KEYBOARD_TO_DEFAULT;
    }
    /**
     * Sets all timing relevant values to be very fast. Timing may not be guaranteed in all cases,
     * violations may occur. This setup shouldn't generally be used. It is mainly used for testing. 
     */
    public static void setAggressiveTiming() {
        KEYBOARD_TO = KEYBOARD_TO_AGGRESSIVE;
    }
    /**
     * Sets all timing relevant values to a value, that allows the user to follow the test
     * on screen for debugging. This option may also be used to identify timing issues in 
     * a test
     */
    public static void setDebugTiming() {
        KEYBOARD_TO = KEYBOARD_TO_DEBUG;
    }
    
    
    public KeyboardRobotImpl(BaseRobot baseRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        this.baseRobot = baseRobot;
    }
 
    @Override
    public void press(KeyCode... keys) {
        Arrays.asList(keys).forEach(k -> {
            WaitForInputEvent w = null;
            if (verify) {
                w = WaitForInputEvent.ofEvent(KEYBOARD_TO, e -> verfiyKey(e, KeyEvent.KEY_PRESSED, k),
                    true);
            }
            pressKey(k);
            if (verify) {
                waitForKey(w, k);
            }
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Override
    public void pressNoWait(KeyCode... keys) {
        Arrays.asList(keys).forEach(this::pressKey);
    }

    @Override
    public void release(KeyCode... keys) {
        if (keys.length == 0) {
            pressedKeys.forEach(k -> {
                WaitForInputEvent w = null;
                if (verify) {
                    w = WaitForInputEvent.ofEvent(KEYBOARD_TO,
                        e -> verfiyKey(e, KeyEvent.KEY_RELEASED, k), true);
                }
                releaseKey(k);
                if (!pressedKeys.contains(OS_SPECIFIC_SHORTCUT) && verify) {
                    waitForKey(w, k);
                }
                WaitForAsyncUtils.waitForFxEvents();
            });
        } else {
            Arrays.asList(keys).forEach(k -> {
                WaitForInputEvent w = null;
                if (verify) {
                    w = WaitForInputEvent.ofEvent(KEYBOARD_TO,
                        e -> verfiyKey(e, KeyEvent.KEY_RELEASED, k), true);
                }
                releaseKey(k);
                if (!pressedKeys.contains(OS_SPECIFIC_SHORTCUT) && verify) {
                    waitForKey(w, k);
                }
                WaitForAsyncUtils.waitForFxEvents();
            });
        }
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
        if (debugKeys) {
            System.out.println("PressKey " + keyCode.name());
        }
        if (pressedKeys.add(realKeyCode)) {
            baseRobot.pressKeyboard(realKeyCode);
        }
    }

    private void releaseKey(KeyCode keyCode) {
        KeyCode realKeyCode = keyCode == KeyCode.SHORTCUT ? OS_SPECIFIC_SHORTCUT : keyCode;
        if (debugKeys) {
            System.out.println("ReleaseKey " + keyCode.name());
        }
        if (pressedKeys.remove(realKeyCode)) {
            baseRobot.releaseKeyboard(realKeyCode);
        }
    }

    private void waitForKey(WaitForInputEvent w, KeyCode code) {
        try {
            w.waitFor();
        } 
        catch (TestFxTimeoutException e) {
            System.err.println("Waiting for key " + code.getName() + " failed. Timing may be corrupted in this test.");
            System.err.println("The key may have been typed outside of the test application!");
        }
    }

    private boolean verfiyKey(Event e, EventType<KeyEvent> type, KeyCode code) {
        boolean ret = e instanceof KeyEvent && ((KeyEvent) e).getEventType().equals(type);
        if (ret && debugKeys) {
            System.out.println("Verified key " + code.getName() + " " + type);
        }
        return ret;
    }

}
