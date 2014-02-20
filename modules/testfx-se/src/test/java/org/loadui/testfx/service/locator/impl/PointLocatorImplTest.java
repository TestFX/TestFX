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
import javafx.scene.Scene;
import javafx.stage.Window;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.PointQuery;

public class PointLocatorImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    PointLocator pointLocator;
    BoundsLocatorStub boundsLocatorStub;

    Bounds nodeBounds;
    Bounds nodeBoundsAfterChange;
    Bounds sceneBounds;
    Bounds sceneBoundsAfterChange;
    Bounds windowBounds;
    Bounds windowBoundsAfterChange;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        boundsLocatorStub = new BoundsLocatorStub();
        pointLocator = new PointLocatorImpl(boundsLocatorStub);

        nodeBounds = new BoundingBox(100, 100, 50, 50);
        nodeBoundsAfterChange = new BoundingBox(200, 200, 50, 50);
        sceneBounds = new BoundingBox(100, 100, 600, 400);
        sceneBoundsAfterChange = new BoundingBox(200, 200, 600, 400);
        windowBounds = new BoundingBox(100, 100, 600, 400);
        windowBoundsAfterChange = new BoundingBox(200, 200, 600, 400);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void pointFor_Bounds_atOffset() {
        // given:
        PointQuery pointQuery = pointLocator.pointFor(new BoundingBox(100, 100, 50, 50));

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(100, 100)));
    }

    @Test
    public void pointFor_Point2D_atOffset() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.pointFor(new Point2D(100, 100));

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(100, 100)));
    }

    @Test
    public void pointFor_Node_atOffset() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.pointFor((Node) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(nodeBounds.getMinX(), nodeBounds.getMinY())));
    }

    @Test @Ignore
    public void pointFor_Node_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.pointFor((Node) null);

        // when:
        boundsLocatorStub.bounds = nodeBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(nodeBoundsAfterChange.getMinX(),
                nodeBoundsAfterChange.getMinY())));
    }

    @Test
    public void pointFor_Scene_atOffset() {
        // given:
        boundsLocatorStub.bounds = sceneBounds;
        PointQuery pointQuery = pointLocator.pointFor((Scene) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(sceneBounds.getMinX(), sceneBounds.getMinY())));
    }

    @Test  @Ignore
    public void pointFor_Scene_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = sceneBounds;
        PointQuery pointQuery = pointLocator.pointFor((Scene) null);

        // when:
        boundsLocatorStub.bounds = sceneBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(sceneBoundsAfterChange.getMinX(),
                sceneBoundsAfterChange.getMinY())));
    }

    @Test
    public void pointFor_Window_atOffset() {
        // given:
        boundsLocatorStub.bounds = windowBounds;
        PointQuery pointQuery = pointLocator.pointFor((Window) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(windowBounds.getMinX(), windowBounds.getMinY())));
    }

    @Test @Ignore
    public void pointFor_Window_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = windowBounds;
        PointQuery pointQuery = pointLocator.pointFor((Window) null);

        // when:
        boundsLocatorStub.bounds = windowBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        MatcherAssert.assertThat(point, Matchers.equalTo(new Point2D(windowBoundsAfterChange.getMinX(),
                windowBoundsAfterChange.getMinY())));
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
