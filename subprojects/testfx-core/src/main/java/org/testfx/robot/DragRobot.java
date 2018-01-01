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

import javafx.scene.input.MouseButton;

import org.testfx.service.query.PointQuery;

public interface DragRobot {

    /**
     * Presses the given mouse button(s) on whatever is under the mouse's current location.
     *
     * @param buttons the mouse buttons to press
     */
    void drag(MouseButton... buttons);

    /**
     * Moves the mouse to the location specified by the given {@link PointQuery#query()} and then presses the
     * given mouse button(s) on whatever is under the mouse's new location.
     *
     * @param pointQuery the pointQuery that specifies where to move the mouse to
     * @param buttons the mouse buttons to press
     */
    void drag(PointQuery pointQuery, MouseButton... buttons);

    /**
     * Releases the mouse at its' current position.
     */
    void drop();

    /**
     * Moves the mouse to the location specified by the given {@link PointQuery#query()} and then
     * releases the mouse.
     *
     * @param pointQuery the pointQuery that specifies where to move the mouse to
     */
    void dropTo(PointQuery pointQuery);

    /**
     * Moves the mouse horizontally by {@code x} and vertically by {@code y} before releasing the mouse.
     *
     * @param x the amount by which to move the mouse horizontally
     * @param y the amount by which to move the mouse vertically
     */
    void dropBy(double x, double y);

}
