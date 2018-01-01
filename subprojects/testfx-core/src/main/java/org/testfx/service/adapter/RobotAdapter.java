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
package org.testfx.service.adapter;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public interface RobotAdapter<T> {
    void robotCreate();
    void robotDestroy();
    T getRobotInstance();

    void keyPress(KeyCode key);
    void keyRelease(KeyCode key);

    Point2D getMouseLocation();
    void mouseMove(Point2D location);
    void mousePress(MouseButton button);
    void mouseRelease(MouseButton button);
    void mouseWheel(int wheelAmount);

    Color getCapturePixelColor(Point2D location);
    Image getCaptureRegion(Rectangle2D region);
}
