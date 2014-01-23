package org.loadui.testfx.robots;

import javafx.scene.input.MouseButton;

public interface MouseRobot {

    /**
     * Presses and holds a mouse button, until explicitly released.
     *
     * @param mouseButtons defaults to the primary mouse button
     */
    void press(MouseButton... mouseButtons);

    /**
     * Releases a pressed mouse button.
     *
     * @param mouseButtons defaults to the primary mouse button.
     */
    void release(MouseButton... mouseButtons);

}
