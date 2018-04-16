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

import java.util.concurrent.TimeoutException;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.BoundsLocatorException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testfx.util.BoundsQueryUtils.scale;

public class BoundsLocatorImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    BoundsLocator boundsLocator;
    Insets windowInsets;
    Stage primaryWindow;
    Scene primaryScene;
    Node nodeInsideOfScene;
    Node nodePartiallyOutsideOfScene;
    Node nodeOutsideOfScene;
    Bounds boundsInsideOfScene;
    Bounds boundsPartiallyOutsideOfScene;
    Bounds boundsOutsideOfScene;

    @After
    public void cleanupSpec() throws TimeoutException {
        FxToolkit.setupFixture(() -> primaryWindow.close());
    }

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupScene(() -> new Scene(new Region(), 600, 400));
        FxToolkit.setupFixture(this::setupStages);
        boundsLocator = new BoundsLocatorImpl();
        windowInsets = calculateWindowInsets(primaryWindow, primaryScene);
    }

    public void setupStages() {
        Pane primarySceneRoot = new AnchorPane();
        primaryScene = new Scene(primarySceneRoot, 600, 400);

        primaryWindow = new Stage();
        primaryWindow.setX(100);
        primaryWindow.setY(100);
        primaryWindow.setScene(primaryScene);

        nodeInsideOfScene = new Rectangle(50, 50, 100, 100);
        nodePartiallyOutsideOfScene = new Rectangle(550, 350, 100, 100);
        nodeOutsideOfScene = new Rectangle(1000, 1000, 100, 100);

        boundsInsideOfScene = nodeInsideOfScene.getLayoutBounds();
        boundsPartiallyOutsideOfScene = nodePartiallyOutsideOfScene.getLayoutBounds();
        boundsOutsideOfScene = nodeOutsideOfScene.getLayoutBounds();

        primarySceneRoot.getChildren().setAll(nodeInsideOfScene, nodePartiallyOutsideOfScene, nodeOutsideOfScene);
        primaryWindow.show();
    }

    @Test
    public void boundsInSceneFor_nodeInsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodeInsideOfScene);

        // then:
        assertThat(bounds, equalTo(new BoundingBox(50, 50, 100, 100)));
    }

    @Test
    public void boundsInSceneFor_nodePartyOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodePartiallyOutsideOfScene);

        // then:
        assertThat(bounds, not(equalTo(new BoundingBox(550, 350, 100, 100))));
        assertThat(bounds, equalTo(new BoundingBox(550, 350, 50, 50)));
    }

    @Test(expected = BoundsLocatorException.class)
    public void boundsInSceneFor_nodeOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodeOutsideOfScene);

        // then:
        assertThat(bounds, equalTo(null));
    }

    @Test
    public void boundsInWindowFor_primaryScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(primaryScene);

        // then:
        Bounds bounds = new BoundingBox(0, 0, 600, 400);
        assertThat(actual, equalTo(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight())));
    }

    @Test
    public void boundsInWindowFor_boundsInsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(boundsInsideOfScene, primaryScene);

        // then:
        Bounds bounds = new BoundingBox(50, 50, 100, 100);
        assertThat(actual, equalTo(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight())));
    }

    @Test
    public void boundsInWindowFor_boundsPartiallyOutsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(boundsPartiallyOutsideOfScene, primaryScene);

        // then:
        Bounds bounds = new BoundingBox(550, 350, 100, 100);
        assertThat(actual, not(equalTo(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight()))));
        bounds = new BoundingBox(550, 350, 50, 50);
        assertThat(actual, equalTo(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight())));
    }

    @Test(expected = BoundsLocatorException.class)
    public void boundsInWindowFor_boundsOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInWindowFor(boundsOutsideOfScene, primaryScene);

        // then:
        assertThat(bounds, equalTo(null));
    }

    @Test
    public void boundsOnScreenFor_primaryWindow() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(primaryWindow);

        // then:
        Bounds bounds = new BoundingBox(100, 100, 600, 400);
        assertThat(actual, equalTo(new BoundingBox(
                bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth() + windowInsets.getLeft() + windowInsets.getRight(),
                bounds.getHeight() + windowInsets.getTop() + windowInsets.getBottom())));
    }

    @Test
    public void boundsOnScreenFor_primaryScene() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(primaryScene);

        // then:
        Bounds bounds = new BoundingBox(100, 100, 600, 400);
        assertThat(actual, equalTo(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight())));
    }

    @Test
    public void boundsOnScreenFor_boundsInsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(boundsInsideOfScene, primaryScene);

        // then:
        Bounds bounds = new BoundingBox(100 + 50, 100 + 50, 100, 100);
        assertThat(actual, equalTo(scale(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight()))));
    }

    @Test
    public void boundsOnScreenFor_boundsPartiallyOutsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(boundsPartiallyOutsideOfScene, primaryScene);

        // then:
        Bounds bounds = new BoundingBox(100 + 550, 100 + 350, 100, 100);
        assertThat(actual, not(equalTo(scale(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight())))));
        bounds = new BoundingBox(100 + 550, 100 + 350, 50, 50);
        assertThat(actual, equalTo(scale(new BoundingBox(
                bounds.getMinX() + windowInsets.getLeft(),
                bounds.getMinY() + windowInsets.getTop(),
                bounds.getWidth(), bounds.getHeight()))));
    }

    @Test(expected = BoundsLocatorException.class)
    public void boundsOnScreenFor_boundsOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsOnScreenFor(boundsOutsideOfScene, primaryScene);

        // then:
        assertThat(bounds, equalTo(null));
    }

    public Insets calculateWindowInsets(Window window, Scene scene) {
        double left = scene.getX();
        double top = scene.getY();
        double right = window.getWidth() - scene.getWidth() - scene.getX();
        double bottom = window.getHeight() - scene.getHeight() - scene.getY();
        return new Insets(top, right, bottom, left);
    }

}
