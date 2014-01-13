package org.loadui.testfx.robots;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;
import org.loadui.testfx.framework.ScreenRobot;

public class KeyboardRobot {

    private final ScreenRobot screenRobot;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    public KeyboardRobot(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    /**
     * Presses and holds a given key, until explicitly released.
     *
     * @param keys
     */
    public void press(KeyCode... keys) {
        for (KeyCode key : keys) {
            if (pressedKeys.add(key)) {
                screenRobot.pressKey(key);
            }
        }
    }

    /**
     * Releases a given key.
     *
     * @param keys
     */
    public void release(KeyCode... keys) {
        if (keys.length == 0) {
            for (KeyCode button : pressedKeys) {
                screenRobot.releaseKey(button);
            }
            pressedKeys.clear();
        }
        else {
            for (KeyCode key : keys) {
                if (pressedKeys.remove(key)) {
                    screenRobot.releaseKey(key);
                }
            }
        }
    }

}
