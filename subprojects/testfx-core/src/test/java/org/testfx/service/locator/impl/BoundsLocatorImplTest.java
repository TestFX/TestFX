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

public class BoundsLocatorImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    BoundsLocator boundsLocator;
    Insets windowInsets;
    Stage primaryWindow;
    Scene primaryScene;
    Node nodeInsideOfScene;
    Node nodePartyOutsideOfScene;
    Node nodeOutsideOfScene;
    Bounds boundsInsideOfScene;
    Bounds boundsPartyOutsideOfScene;
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
        nodePartyOutsideOfScene = new Rectangle(550, 350, 100, 100);
        nodeOutsideOfScene = new Rectangle(1000, 1000, 100, 100);

        boundsInsideOfScene = nodeInsideOfScene.getLayoutBounds();
        boundsPartyOutsideOfScene = nodePartyOutsideOfScene.getLayoutBounds();
        boundsOutsideOfScene = nodeOutsideOfScene.getLayoutBounds();

        primarySceneRoot.getChildren().setAll(
            nodeInsideOfScene, nodePartyOutsideOfScene, nodeOutsideOfScene
        );

        primaryWindow.show();
    }

    @Test
    public void boundsInSceneFor_nodeInsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodeInsideOfScene);

        // then:
        assertThat(bounds, equalTo(bounds(50, 50, 100, 100)));
    }

    @Test
    public void boundsInSceneFor_nodePartyOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodePartyOutsideOfScene);

        // then:
        assertThat(bounds, not(equalTo(bounds(550, 350, 100, 100))));
        assertThat(bounds, equalTo(bounds(550, 350, 50, 50)));
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
        Bounds bounds = boundsLocator.boundsInWindowFor(primaryScene);

        // then:
        assertThat(bounds, equalTo(boundsWithOffset(0, 0, 600, 400, windowInsets)));
    }

    @Test
    public void boundsInWindowFor_boundsInsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInWindowFor(boundsInsideOfScene, primaryScene);

        // then:
        assertThat(bounds, equalTo(boundsWithOffset(50, 50, 100, 100, windowInsets)));
    }

    @Test
    public void boundsInWindowFor_boundsPartyOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInWindowFor(boundsPartyOutsideOfScene, primaryScene);

        // then:
        assertThat(bounds, not(equalTo(boundsWithOffset(550, 350, 100, 100, windowInsets))));
        assertThat(bounds, equalTo(boundsWithOffset(550, 350, 50, 50, windowInsets)));
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
        Bounds bounds = boundsLocator.boundsOnScreenFor(primaryWindow);

        // then:
        assertThat(bounds, equalTo(boundsWithInsets(100, 100, 600, 400, windowInsets)));
    }

    @Test
    public void boundsOnScreenFor_primaryScene() {
        // when:
        Bounds bounds = boundsLocator.boundsOnScreenFor(primaryScene);

        // then:
        assertThat(bounds, equalTo(boundsWithOffset(100, 100, 600, 400, windowInsets)));
    }

    @Test
    public void boundsOnScreenFor_boundsInsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsOnScreenFor(boundsInsideOfScene, primaryScene);

        // then:
        assertThat(bounds, equalTo(boundsWithOffset(100 + 50, 100 + 50, 100, 100, windowInsets)));
    }

    @Test
    public void boundsOnScreenFor_boundsPartyOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsOnScreenFor(boundsPartyOutsideOfScene, primaryScene);

        // then:
        assertThat(bounds, not(equalTo(boundsWithOffset(100 + 550, 100 + 350, 100, 100,
            windowInsets))));
        assertThat(bounds, equalTo(boundsWithOffset(100 + 550, 100 + 350, 50, 50, windowInsets)));
    }

    @Test(expected = BoundsLocatorException.class)
    public void boundsOnScreenFor_boundsOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsOnScreenFor(boundsOutsideOfScene, primaryScene);

        // then:
        assertThat(bounds, equalTo(null));
    }

    public Bounds bounds(double x, double y, double width, double height) {
        return new BoundingBox(x, y, width, height);
    }

    public Bounds boundsWithOffset(double x, double y, double width, double height, Insets insets) {
        Bounds bounds = bounds(x, y, width, height);
        return withOffset(bounds, insets);
    }

    public Bounds boundsWithInsets(double x, double y, double width, double height, Insets insets) {
        Bounds bounds = bounds(x, y, width, height);
        return withInsets(bounds, insets);
    }

    public Bounds withOffset(Bounds bounds, Insets insets) {
        return new BoundingBox(bounds.getMinX() + insets.getLeft(), bounds.getMinY() + insets.getTop(),
            bounds.getWidth(), bounds.getHeight());
    }

    public Bounds withInsets(Bounds bounds, Insets insets) {
        return new BoundingBox(bounds.getMinX(), bounds.getMinY(),
            bounds.getWidth() + insets.getLeft() + insets.getRight(),
            bounds.getHeight() + insets.getTop() + insets.getBottom());
    }

    public Insets calculateWindowInsets(Window window, Scene scene) {
        double left = scene.getX();
        double top = scene.getY();
        double right = window.getWidth() - scene.getWidth() - scene.getX();
        double bottom = window.getHeight() - scene.getHeight() - scene.getY();
        return new Insets(top, right, bottom, left);
    }

}
