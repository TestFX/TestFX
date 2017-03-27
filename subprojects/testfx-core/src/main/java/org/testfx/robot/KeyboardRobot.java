/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.robot;

import java.util.Set;
import javafx.scene.input.KeyCode;

public interface KeyboardRobot {

    /**
     * Presses given keys, until explicitly released via {@link #release(KeyCode...)}. Once pressed,
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is called.
     *
     * @param keys the key codes
     */
    void press(KeyCode... keys);

    /**
     * Presses given keys, until explicitly released via {@link #release(KeyCode...)}.
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     *
     * @param keys the key codes
     */
    void pressNoWait(KeyCode... keys);

    /**
     * Gets the keys that have been pressed and not released.
     *
     * @return an unmodifiable set of the keys that have been pressed but not released.
     */
    Set<KeyCode> getPressedKeys();

    /**
     * Releases given keys. Once released, {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is called.
     * <em>Note:</em> passing in an empty {@code KeyCode[]} will release all pressed keys.
     *
     * @param keys the key codes
     */
    void release(KeyCode... keys);

    /**
     * Releases given keys. {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     * <em>Note:</em> passing in an empty {@code KeyCode[]} will release all pressed keys.
     *
     * @param keys the key codes
     */
    void releaseNoWait(KeyCode... keys);

}
