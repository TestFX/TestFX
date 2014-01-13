package org.loadui.testfx.robots;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.MouseButton;
import org.loadui.testfx.framework.ScreenRobot;

public class MouseRobot {

    private final ScreenRobot screenRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();

    public MouseRobot(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    /**
     * Presses and holds a mouse button, until explicitly released.
     *
     * @param buttons defaults to the primary mouse button.
     */
    public void press(MouseButton... buttons) {
        if (buttons.length == 0) {
            press(MouseButton.PRIMARY);
        }

        for (MouseButton button : buttons) {
            if (pressedButtons.add(button)) {
                screenRobot.pressMouse(button);
            }
        }
    }

    /**
     * Releases a pressed mouse button.
     *
     * @param buttons defaults to the primary mouse button.
     */
    public void release(MouseButton... buttons) {
        if (buttons.length == 0) {
            for (MouseButton button : pressedButtons) {
                screenRobot.releaseMouse(button);
            }
            pressedButtons.clear();
        }
        else {
            for (MouseButton button : buttons) {
                if (pressedButtons.remove(button)) {
                    screenRobot.releaseMouse(button);
                }
            }
        }
    }

}
