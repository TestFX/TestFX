package org.loadui.testfx.robots;

import javafx.scene.input.KeyCode;

public interface KeyboardRobot {

    /**
     * Presses and holds a given key, until explicitly released.
     *
     * @param keyCodes the key codes
     */
    public void press(KeyCode... keyCodes);

    /**
     * Releases a given key.
     *
     * @param keyCodes the key codes
     */
    public void release(KeyCode... keyCodes);

}
