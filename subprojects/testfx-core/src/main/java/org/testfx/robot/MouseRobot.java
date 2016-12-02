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
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;


public interface MouseRobot {

    /**
     * Presses given buttons, until explicitly released.
     *
     * @param buttons the mouse buttons
     */
    public void press(MouseButton... buttons);
    public void pressNoWait(MouseButton... buttons);

    /**
     * Gets the buttons that have been pressed but not yet released.
     * @return an unmodifiable set of the pressed buttons
     */
    public Set<MouseButton> getPressedButtons();

    /**
     * Releases given buttons.
     *
     * @param buttons the mouse buttons
     */
    public void release(MouseButton... buttons);
    public void releaseNoWait(MouseButton... buttons);

    /**
     * Moves mouse to given location.
     *
     * @param location the location to move
     */
    public void move(Point2D location);
    public void moveNoWait(Point2D location);

    /**
     * Scrolls mouse wheel given amount.
     *
     * @param wheelAmount the amount to scroll
     */
    public void scroll(int wheelAmount);
    public void scrollNoWait(int wheelAmount);

}
