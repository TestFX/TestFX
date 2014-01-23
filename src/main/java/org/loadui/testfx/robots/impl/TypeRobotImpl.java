package org.loadui.testfx.robots.impl;

import javafx.scene.input.KeyCode;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.utils.KeyCodeUtils;

public class TypeRobotImpl implements TypeRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private KeyboardRobot keyboardRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public TypeRobotImpl(KeyboardRobot keyboardRobot) {
        this.keyboardRobot = keyboardRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void type(KeyCode... keys) {
        keyboardRobot.press(keys);
        keyboardRobot.release(keys);
    }

    @Override
    public void type(char character) {
        KeyCode keyCode = KeyCodeUtils.findKeyCode(character);
        if (isNotUpperCase(character)) {
            typeLowerCase(keyCode);
        }
        else {
            typeUpperCase(keyCode);
        }
    }

    @Override
    public void type(String text) {
        for (int index = 0; index < text.length(); index++) {
            type(text.charAt(index));
            waitBetweenCharacters(25);
        }
    }

    @Override
    public void erase(int characters) {
        for (int index = 0; index < characters; index++) {
            type(KeyCode.BACK_SPACE);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isNotUpperCase(char character) {
        return !Character.isUpperCase(character);
    }

    private void typeLowerCase(KeyCode keyCode) {
        type(keyCode);
    }

    private void typeUpperCase(KeyCode keyCode) {
        keyboardRobot.press(KeyCode.SHIFT);
        type(keyCode);
        keyboardRobot.release(KeyCode.SHIFT);
    }

    private void waitBetweenCharacters(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ignore) {}
    }

}
