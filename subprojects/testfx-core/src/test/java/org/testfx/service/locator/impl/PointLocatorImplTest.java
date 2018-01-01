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
package org.testfx.service.locator.impl;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.query.PointQuery;

import static org.hamcrest.MatcherAssert.assertThat;

public class PointLocatorImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    PointLocator pointLocator;
    BoundsLocatorStub boundsLocatorStub;
    Bounds nodeBounds;
    Bounds nodeBoundsAfterChange;
    Bounds sceneBounds;
    Bounds sceneBoundsAfterChange;
    Bounds windowBounds;
    Bounds windowBoundsAfterChange;

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

    @Test
    public void pointFor_Bounds_atOffset() {
        // given:
        PointQuery pointQuery = pointLocator.point(new BoundingBox(100, 100, 50, 50));

        // when:
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(new Point2D(100, 100)));
    }

    @Test
    public void pointFor_Point2D_atOffset() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.point(new Point2D(100, 100));

        // when:
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(new Point2D(100, 100)));
    }

    @Test
    public void pointFor_Node_atOffset() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.point((Node) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(nodeBounds)));
    }

    @Test
    public void pointFor_Node_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = nodeBounds;
        PointQuery pointQuery = pointLocator.point((Node) null);

        // when:
        boundsLocatorStub.bounds = nodeBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(nodeBoundsAfterChange)));
    }

    @Test
    public void pointFor_Scene_atOffset() {
        // given:
        boundsLocatorStub.bounds = sceneBounds;
        PointQuery pointQuery = pointLocator.point((Scene) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(sceneBounds)));
    }

    @Test
    public void pointFor_Scene_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = sceneBounds;
        PointQuery pointQuery = pointLocator.point((Scene) null);

        // when:
        boundsLocatorStub.bounds = sceneBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(sceneBoundsAfterChange)));
    }

    @Test
    public void pointFor_Window_atOffset() {
        // given:
        boundsLocatorStub.bounds = windowBounds;
        PointQuery pointQuery = pointLocator.point((Window) null);

        // when:
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(windowBounds)));
    }

    @Test
    public void pointFor_Window_atOffset_afterChange() {
        // given:
        boundsLocatorStub.bounds = windowBounds;
        PointQuery pointQuery = pointLocator.point((Window) null);

        // when:
        boundsLocatorStub.bounds = windowBoundsAfterChange;
        Point2D point = pointQuery.atOffset(0, 0).query();

        // then:
        assertThat(point, CoreMatchers.equalTo(topLeftPointFrom(windowBoundsAfterChange)));
    }

    public Point2D topLeftPointFrom(Bounds bounds) {
        return new Point2D(bounds.getMinX(), bounds.getMinY());
    }

    public static class BoundsLocatorStub implements BoundsLocator {
        public Bounds bounds;

        @Override
        public Bounds boundsInSceneFor(Node node) {
            return bounds;
        }

        @Override
        public Bounds boundsInWindowFor(Scene scene) {
            return bounds;
        }

        @Override
        public Bounds boundsInWindowFor(Bounds boundsInScene, Scene scene) {
            return bounds;
        }

        @Override
        public Bounds boundsOnScreenFor(Node node) {
            return bounds;
        }

        @Override
        public Bounds boundsOnScreenFor(Scene scene) {
            return bounds;
        }

        @Override
        public Bounds boundsOnScreenFor(Window window) {
            return bounds;
        }

        @Override
        public Bounds boundsOnScreenFor(Bounds boundsInScene, Scene scene) {
            return bounds;
        }
    }

}
