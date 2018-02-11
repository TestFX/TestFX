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
package org.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.testfx.service.query.PointQuery;

/**
 * Interface for constructing {@link PointQuery} objects that can be used to return a specific
 * point (in terms of the screen) somewhere in the bounds of the provided object.
 */
public interface PointLocator {

    /**
     *
     * @param bounds the initial bounds with which to construct a {@link PointQuery}
     * @return a {@link PointQuery} with the given bounds as the initial bounds
     */
    PointQuery point(Bounds bounds);

    /**
     *
     * @param point to convert into a {@link Bounds} object
     * @return a {@link PointQuery} whose bounds x and y values are the given point and whose width/height = 0
     */
    PointQuery point(Point2D point);

    /**
     * Returns the center of the given {@code Node} in screen coordinates.
     *
     * @param node the node
     * @return a {@link PointQuery} with the node's bounds (in terms of the screen) as the initial bounds
     */
    PointQuery point(Node node);

    /**
     *
     * @param scene the scene
     * @return a {@link PointQuery} with the scene's bounds (in terms of the screen) as the initial bounds
     */
    PointQuery point(Scene scene);

    /**
     *
     * @param window the window
     * @return a {@link PointQuery} with the window's bounds (in terms of the screen) as the initial bounds
     */
    PointQuery point(Window window);
}
