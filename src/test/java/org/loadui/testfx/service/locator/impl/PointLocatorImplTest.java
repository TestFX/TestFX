/*
 * Copyright 2013 SmartBear Software
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
package org.loadui.testfx.service.locator.impl;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.PointQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PointLocatorImplTest extends GuiTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    PointLocator pointLocator;
    BoundsLocatorStub boundsLocatorStub;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        boundsLocatorStub = new BoundsLocatorStub();
        pointLocator = new PointLocatorImpl(boundsLocatorStub);
    }

    @Override
    protected Parent getRootNode() {
        return new AnchorPane();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void pointFor_atOffset() {
        // given:
        boundsLocatorStub.bounds = new BoundingBox(100, 100, 0, 0);
        PointQuery pointQuery = pointLocator.pointFor((Node) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        assertThat(point, equalTo(new Point2D(100, 100)));
    }

    @Test
    public void pointFor_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = new BoundingBox(100, 100, 0, 0);
        PointQuery pointQuery = pointLocator.pointFor((Node) null);

        // when:
        boundsLocatorStub.bounds = new BoundingBox(200, 200, 0, 0);
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        assertThat(point, equalTo(new Point2D(200, 200)));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER CLASSES.
    //---------------------------------------------------------------------------------------------

    public static class BoundsLocatorStub implements BoundsLocator {
        public Bounds bounds;

        public Bounds boundsInSceneFor(Node node) {
            return bounds;
        }

        public Bounds boundsInWindowFor(Scene scene) {
            return bounds;
        }

        public Bounds boundsInWindowFor(Bounds boundsInScene, Scene scene) {
            return bounds;
        }

        public Bounds boundsOnScreenFor(Node node) {
            return bounds;
        }

        public Bounds boundsOnScreenFor(Scene scene) {
            return bounds;
        }

        public Bounds boundsOnScreenFor(Window window) {
            return bounds;
        }

        public Bounds boundsOnScreenFor(Bounds boundsInScene, Scene scene) {
            return bounds;
        }
    }

}
