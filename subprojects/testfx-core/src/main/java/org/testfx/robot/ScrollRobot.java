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

import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;

public interface ScrollRobot {

    /**
     * Scrolls vertically by amount (in terms of ticks of a mouse wheel): if {@code amount >= 0}, up; otherwise, down.
     *
     * @param amount of a mouse wheel's scroll ticks
     */
    void scroll(int amount);

    /**
     * Scrolls vertically by amount (in terms of ticks of a mouse wheel) in given direction.
     *
     * @param positiveAmount the number of scroll ticks
     * @param direction whether to scroll up or down
     */
    void scroll(int positiveAmount,
                VerticalDirection direction);

    /**
     * Scrolls up by amount (in terms of ticks of a mouse wheel).
     * .
     * @param positiveAmount the number of scroll ticks
     */
    void scrollUp(int positiveAmount);

    /**
     * Scrolls down by amount (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks
     */
    void scrollDown(int positiveAmount);

    /**
     * Scrolls horizontally by amount (in terms of ticks of a mouse wheel) in given direction.
     *
     * @param positiveAmount the number of scroll ticks
     * @param direction whether to scroll left or right
     */
    void scroll(int positiveAmount,
                HorizontalDirection direction);

    /**
     * Scrolls left by amount (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks
     */
    void scrollLeft(int positiveAmount);

    /**
     * Scrolls right by amount (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks
     */
    void scrollRight(int positiveAmount);
}
