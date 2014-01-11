package org.loadui.testfx.robots;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;
import org.loadui.testfx.ScreenController;

public class KeyboardRobot {

    private final ScreenController controller;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    public KeyboardRobot(final ScreenController controller) {
        this.controller = controller;
    }

    /**
     * Presses and holds a given key, until explicitly released.
     *
     * @param keys
     */
    public void press(KeyCode... keys) {
        for (KeyCode key : keys) {
            if (pressedKeys.add(key)) {
                controller.press(key);
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
                controller.release(button);
            }
            pressedKeys.clear();
        }
        else {
            for (KeyCode key : keys) {
                if (pressedKeys.remove(key)) {
                    controller.release(key);
                }
            }
        }
    }

}
