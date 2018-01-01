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

import java.util.Set;
import javafx.scene.input.KeyCode;

public interface KeyboardRobot {

    /**
     * Presses the given keys, until explicitly released via {@link #release(KeyCode...)}. Once pressed,
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is called.
     *
     * @param keys the key codes to press
     */
    void press(KeyCode... keys);

    /**
     * Presses the given keys, until explicitly released via {@link #release(KeyCode...)}.
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     *
     * @param keys the key codes to press without waiting afterwards
     */
    void pressNoWait(KeyCode... keys);

    /**
     * Gets the keys that have been pressed and not yet released.
     *
     * @return an (unmodifiable) containing the keys that have been pressed (but not yet) released.
     */
    Set<KeyCode> getPressedKeys();

    /**
     * Releases the given keys. Once released, {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is called.
     * <p>
     * <em>Note:</em> passing in an empty {@code KeyCode[]} will release all pressed keys.
     *
     * @param keys the key codes to release
     */
    void release(KeyCode... keys);

    /**
     * Releases the given keys. {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     * <p>
     * <em>Note:</em> passing in an empty {@code KeyCode[]} will release all pressed keys.
     *
     * @param keys the key codes to release without waiting afterwards
     */
    void releaseNoWait(KeyCode... keys);

}
