package org.loadui.testfx.robots;

import javafx.scene.input.KeyCode;

public interface TypeRobot {

    public void type(KeyCode... keys);

    public void type(char character);

    /**
     * Types the given text on the keyboard.
     *
     * Note: Typing depends on the operating system keyboard layout.
     *
     * @param text the text
     */
    public void type(String text);

    public void erase(int characters);

}
