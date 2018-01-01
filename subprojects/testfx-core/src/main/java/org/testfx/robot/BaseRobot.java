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

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface BaseRobot {

    void pressKeyboard(KeyCode key);

    void releaseKeyboard(KeyCode key);

    void typeKeyboard(Scene scene,
                      KeyCode key,
                      String character);

    /**
     *
     * @return the current mouse location
     */
    Point2D retrieveMouse();

    void moveMouse(Point2D point);

    void scrollMouse(int amount);

    void pressMouse(MouseButton button);

    void releaseMouse(MouseButton button);

    /**
     *
     * @param region the given bounds for the image
     * @return a screen capture of the given region
     */
    Image captureRegion(Rectangle2D region);

}
