package org.loadui.testfx.robots.impl;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

import org.loadui.testfx.framework.ScreenRobot;
import org.loadui.testfx.robots.KeyboardRobot;

public class KeyboardRobotImpl implements KeyboardRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ScreenRobot screenRobot;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public KeyboardRobotImpl(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(KeyCode... keyCodes) {
        for (KeyCode keyCode : keyCodes) {
            if (pressedKeys.add(keyCode)) {
                screenRobot.pressKey(keyCode);
            }
        }
    }

    @Override
    public void release(KeyCode... keyCodes) {
        if (keyCodes.length == 0) {
            for (KeyCode button : pressedKeys) {
                screenRobot.releaseKey(button);
            }
            pressedKeys.clear();
        }
        else {
            for (KeyCode keyCode : keyCodes) {
                if (pressedKeys.remove(keyCode)) {
                    screenRobot.releaseKey(keyCode);
                }
            }
        }
    }

}
