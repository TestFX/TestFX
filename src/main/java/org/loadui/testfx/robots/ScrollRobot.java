package org.loadui.testfx.robots;

import javafx.geometry.VerticalDirection;

public interface ScrollRobot {
    public void scroll(int amount);

    /**
     * Scrolls the mouse-wheel a given number of notches in a direction.
     *
     * @param positiveAmount the number of notches to scroll
     * @param direction the direction to scroll
     */
    public void scroll(int positiveAmount, VerticalDirection direction);

    public void scrollUp(int positiveAmount);

    public void scrollDown(int positiveAmount);
}
