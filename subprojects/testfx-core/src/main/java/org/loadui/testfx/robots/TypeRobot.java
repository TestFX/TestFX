/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.robots;

import javafx.scene.input.KeyCode;

public interface TypeRobot {

    /**
     * Types given keys one after the other.
     *
     * @param keyCodes the key codes
     */
    public void type(KeyCode... keyCodes);

    /**
     * Holds given keys, until explicitly released.
     *
     * @param keyCodes the key codes
     */
    public void hold(KeyCode... keyCodes);

    /**
     * Types given keys one after the other, then releases all currently held keys.
     *
     * @param keyCodes the key codes
     */
    public void andType(KeyCode... keyCodes);

    /**
     * Pushes a given key multiple times.
     *
     * @param keyCode the key code
     * @param times number of times
     */
    public void push(KeyCode keyCode, int times);

    /**
     * Pushes a given key combination.
     *
     * @param keyCodes the key codes
     */
    public void push(KeyCode... keyCodes);

    /**
     * Writes given text characters one after the other.
     *
     * @param text the text characters
     */
    public void write(String text);

    /**
     * Writes a given text character.
     *
     * @param character the text character
     */
    public void write(char character);

}
