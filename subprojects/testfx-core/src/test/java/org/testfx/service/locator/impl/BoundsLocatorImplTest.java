/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.beans.InvalidationListener;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.TestFXRule;
import org.testfx.api.FxToolkit;
import org.testfx.internal.JavaVersionAdapter;
import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.BoundsLocatorException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests that {@link BoundsLocator} correctly computes the scene, window,
 * and screen bounds for nodes. These bounds must be correct regardless of
 * the DPI scaling.
 */
public class BoundsLocatorImplTest {

    private static final double WINDOW_TRANSLATE_X = 100;
    private static final double WINDOW_TRANSLATE_Y = 100;
    private static final double INSIDE_SCENE_X = 50;
    private static final double INSIDE_SCENE_Y = 50;
    private static final double INSIDE_SCENE_WIDTH = 100;
    private static final double INSIDE_SCENE_HEIGHT = 100;
    private static final double PARTIAL_OUTSIDE_SCENE_WIDTH = 100;
    private static final double PARTIAL_OUTSIDE_SCENE_HEIGHT = 100;

    private static double sceneWidth = 600;
    private static double sceneHeight = 400;
    private static double partialOutsideSceneX = 550;
    private static double partialOutsideSceneY = 350;

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    BoundsLocator boundsLocator;
    Insets windowInsets;
    Stage primaryWindow;
    Scene primaryScene;
    Node nodeInsideOfScene;
    Node nodePartiallyOutsideOfScene;
    Node nodeOutsideOfScene;
    boolean waitForNewSize;

    @After
    public void cleanupSpec() throws TimeoutException {
        FxToolkit.setupFixture(() -> primaryWindow.close());
    }

    @Before
    public void setup() throws TimeoutException, InterruptedException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupScene(() -> new Scene(new Region(), sceneWidth, sceneHeight));
        FxToolkit.setupFixture(this::setupStages);
        if (waitForNewSize) {
            Thread.sleep(5000);
        }
        boundsLocator = new BoundsLocatorImpl();
        windowInsets = calculateWindowInsets(primaryWindow, primaryScene);
    }

    private void setupStages() {
        primaryWindow = new Stage();
        CountDownLatch setSceneLatch = new CountDownLatch(1);
        InvalidationListener invalidationListener = observable -> setSceneLatch.countDown();
        primaryWindow.sceneProperty().addListener(observable -> {
            setSceneLatch.countDown();
            primaryWindow.sceneProperty().removeListener(invalidationListener);
        });
        nodeInsideOfScene = new Rectangle(INSIDE_SCENE_X, INSIDE_SCENE_Y, INSIDE_SCENE_WIDTH, INSIDE_SCENE_HEIGHT);
        nodePartiallyOutsideOfScene = new Rectangle(partialOutsideSceneX, partialOutsideSceneY,
                PARTIAL_OUTSIDE_SCENE_WIDTH, PARTIAL_OUTSIDE_SCENE_HEIGHT);
        nodeOutsideOfScene = new Rectangle(sceneWidth + 300, sceneHeight + 300, 100, 100);
        AnchorPane nodeContainer = new AnchorPane(nodeInsideOfScene, nodePartiallyOutsideOfScene, nodeOutsideOfScene);
        primaryScene = new Scene(nodeContainer, sceneWidth, sceneHeight);

        // On Windows + AWT Robot + HiDPI the scene size changes from (600, 400) to (506, 366.5) - WHY!?
        // The scene size changing also makes the "partial" outside bounds tests fail because they are
        // totally outside the bounds, no longer partially.
        if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("win") &&
                System.getProperty("testfx.robot", "awt").toLowerCase(Locale.US).equals("awt") &&
                (JavaVersionAdapter.getScreenScaleX() != 1d || JavaVersionAdapter.getScreenScaleY() != 1d)) {
            primaryScene.widthProperty().addListener(
                (observable, oldVal, newVal) -> {
                    waitForNewSize = true;
                    partialOutsideSceneX = partialOutsideSceneX - (oldVal.doubleValue() - newVal.doubleValue());
                    nodeContainer.getChildren().remove(nodePartiallyOutsideOfScene);
                    nodePartiallyOutsideOfScene = new Rectangle(partialOutsideSceneX,
                            nodePartiallyOutsideOfScene.getBoundsInLocal().getMinY(), PARTIAL_OUTSIDE_SCENE_WIDTH,
                            nodePartiallyOutsideOfScene.getBoundsInLocal().getHeight());
                    nodeContainer.getChildren().add(1, nodePartiallyOutsideOfScene);
                    sceneWidth = newVal.doubleValue();
                });
            primaryScene.heightProperty().addListener(
                (observable, oldVal, newVal) -> {
                    waitForNewSize = true;
                    partialOutsideSceneY = partialOutsideSceneY -
                            (oldVal.doubleValue() - newVal.doubleValue());
                    nodeContainer.getChildren().remove(nodePartiallyOutsideOfScene);
                    nodePartiallyOutsideOfScene = new Rectangle(
                            nodePartiallyOutsideOfScene.getBoundsInLocal().getMinX(), partialOutsideSceneY,
                            nodePartiallyOutsideOfScene.getBoundsInLocal().getWidth(), PARTIAL_OUTSIDE_SCENE_HEIGHT);
                    nodeContainer.getChildren().add(1, nodePartiallyOutsideOfScene);
                    sceneHeight = newVal.doubleValue();
                });
        }
        primaryWindow.setX(WINDOW_TRANSLATE_X);
        primaryWindow.setY(WINDOW_TRANSLATE_X);
        primaryWindow.setScene(primaryScene);
        try {
            if (!setSceneLatch.await(10, TimeUnit.SECONDS)) {
                fail("Timeout while waiting for scene to be set on stage.");
            }
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }
        primaryWindow.show();
    }

    @Test
    public void boundsInSceneFor_nodeInsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodeInsideOfScene);

        // then:
        assertThat(bounds, equalTo(new BoundingBox(INSIDE_SCENE_X, INSIDE_SCENE_Y,
                INSIDE_SCENE_WIDTH, INSIDE_SCENE_HEIGHT)));
    }

    @Test
    public void boundsInSceneFor_nodePartiallyOutsideOfScene() {
        // when:
        Bounds bounds = boundsLocator.boundsInSceneFor(nodePartiallyOutsideOfScene);

        // then:
        assertThat(bounds, equalTo(new BoundingBox(partialOutsideSceneX, partialOutsideSceneY,
                (partialOutsideSceneX + PARTIAL_OUTSIDE_SCENE_WIDTH) - sceneWidth,
                (partialOutsideSceneY + PARTIAL_OUTSIDE_SCENE_HEIGHT) - sceneHeight)));
    }

    @Test
    public void boundsInSceneFor_nodeOutsideOfScene() {
        assertThatThrownBy(() -> boundsLocator.boundsInSceneFor(nodeOutsideOfScene))
                .isExactlyInstanceOf(BoundsLocatorException.class);
    }

    @Test
    public void boundsInWindowFor_primaryScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(windowInsets.getLeft(),
                windowInsets.getTop(), sceneWidth, sceneHeight)));
    }

    @Test
    public void boundsInWindowFor_boundsInsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(nodeInsideOfScene.getLayoutBounds(), primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(INSIDE_SCENE_X + windowInsets.getLeft(),
                INSIDE_SCENE_Y + windowInsets.getTop(),
                INSIDE_SCENE_WIDTH, INSIDE_SCENE_HEIGHT)));
    }

    @Test
    public void boundsInWindowFor_boundsPartiallyOutsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsInWindowFor(nodePartiallyOutsideOfScene.getLayoutBounds(), primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(
                partialOutsideSceneX + windowInsets.getLeft(), partialOutsideSceneY + windowInsets.getTop(),
                (partialOutsideSceneX + PARTIAL_OUTSIDE_SCENE_WIDTH) - sceneWidth,
                (partialOutsideSceneY + PARTIAL_OUTSIDE_SCENE_HEIGHT) - sceneHeight)));
    }

    @Test
    public void boundsInWindowFor_boundsOutsideOfScene() {
        assertThatThrownBy(() ->  boundsLocator.boundsInWindowFor(nodeOutsideOfScene.getLayoutBounds(), primaryScene))
                .isExactlyInstanceOf(BoundsLocatorException.class);
    }

    @Test
    public void boundsOnScreenFor_primaryWindow() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(primaryWindow);

        // then:
        assertThat(actual, equalTo(new BoundingBox(
                WINDOW_TRANSLATE_X, WINDOW_TRANSLATE_Y,
                sceneWidth + windowInsets.getLeft() + windowInsets.getRight(),
                sceneHeight + windowInsets.getTop() + windowInsets.getBottom())));
    }

    @Test
    public void boundsOnScreenFor_primaryScene() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(WINDOW_TRANSLATE_X + windowInsets.getLeft(),
                WINDOW_TRANSLATE_Y + windowInsets.getTop(),
                sceneWidth, sceneHeight)));
    }

    @Test
    public void boundsOnScreenFor_boundsInsideOfScene() {
        Bounds actual = boundsLocator.boundsOnScreenFor(nodeInsideOfScene.getLayoutBounds(), primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(
                WINDOW_TRANSLATE_X + INSIDE_SCENE_X + windowInsets.getLeft(),
                WINDOW_TRANSLATE_Y + INSIDE_SCENE_Y + windowInsets.getTop(),
                INSIDE_SCENE_WIDTH, INSIDE_SCENE_HEIGHT)));
    }

    @Test
    public void boundsOnScreenFor_boundsPartiallyOutsideOfScene() {
        // when:
        Bounds actual = boundsLocator.boundsOnScreenFor(nodePartiallyOutsideOfScene.getLayoutBounds(), primaryScene);

        // then:
        assertThat(actual, equalTo(new BoundingBox(
                WINDOW_TRANSLATE_X + partialOutsideSceneX + windowInsets.getLeft(),
                WINDOW_TRANSLATE_Y + partialOutsideSceneY + windowInsets.getTop(),
                (partialOutsideSceneX + PARTIAL_OUTSIDE_SCENE_WIDTH) - sceneWidth,
                (partialOutsideSceneY + PARTIAL_OUTSIDE_SCENE_HEIGHT) - sceneHeight)));
    }

    @Test
    public void boundsOnScreenFor_boundsOutsideOfScene() {
        assertThatThrownBy(() -> boundsLocator.boundsOnScreenFor(nodeOutsideOfScene.getLayoutBounds(), primaryScene))
                .isExactlyInstanceOf(BoundsLocatorException.class);
    }

    private static Insets calculateWindowInsets(Window window, Scene scene) {
        double left = scene.getX();
        double top = scene.getY();
        double right = window.getWidth() - scene.getWidth() - scene.getX();
        double bottom = window.getHeight() - scene.getHeight() - scene.getY();
        return new Insets(top, right, bottom, left);
    }


}
