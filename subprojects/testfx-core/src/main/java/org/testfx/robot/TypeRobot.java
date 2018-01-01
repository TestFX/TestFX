/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.robot;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public interface TypeRobot {

    /**
     * Pushes a given key combination.
     *
     * @param combination the combination to push
     */
    void push(KeyCode... combination);

    /**
     * Pushes a given key combination.
     *
     * @param combination the combination to push
     */
    void push(KeyCodeCombination combination);

    /**
     * Types given keys one after the other.
     *
     * @param keyCodes the key codes to type
     */
    void type(KeyCode... keyCodes);

    /**
     * Types a given key multiple times.
     *
     * @param keyCode the key code to type {@code times} times
     * @param times number of times to type the {@code keyCode}
     */
    void type(KeyCode keyCode, int times);

}
