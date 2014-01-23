package org.loadui.testfx.robots.impl;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseButton;

import org.loadui.testfx.framework.ScreenRobot;
import org.loadui.testfx.robots.MouseRobot;

public class MouseRobotImpl implements MouseRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ScreenRobot screenRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MouseRobotImpl(final ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void press(MouseButton... mouseButtons) {
        if (mouseButtons.length == 0) {
            press(MouseButton.PRIMARY);
        }

        for (MouseButton mouseButton : mouseButtons) {
            if (pressedButtons.add(mouseButton)) {
                screenRobot.pressMouse(mouseButton);
            }
        }
    }

    @Override
    public void release(MouseButton... mouseButtons) {
        if (mouseButtons.length == 0) {
            for (MouseButton mouseButton : pressedButtons) {
                screenRobot.releaseMouse(mouseButton);
            }
            pressedButtons.clear();
        }
        else {
            for (MouseButton button : mouseButtons) {
                if (pressedButtons.remove(button)) {
                    screenRobot.releaseMouse(button);
                }
            }
        }
    }

}
