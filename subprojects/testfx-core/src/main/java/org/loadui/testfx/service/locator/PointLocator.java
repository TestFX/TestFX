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
package org.loadui.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.loadui.testfx.service.query.PointQuery;

public interface PointLocator {
    PointQuery pointFor(Bounds bounds);

    PointQuery pointFor(Point2D point);

    PointQuery pointFor(Node node);

    PointQuery pointFor(Scene scene);

    PointQuery pointFor(Window window);
}
