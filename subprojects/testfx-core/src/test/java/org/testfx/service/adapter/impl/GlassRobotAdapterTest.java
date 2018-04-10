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
package org.testfx.service.adapter.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.locator.impl.BoundsLocatorImpl;
import org.testfx.service.locator.impl.PointLocatorImpl;
import org.testfx.util.BoundsQueryUtils;
import org.testfx.util.WaitForAsyncUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.sleep;

public class GlassRobotAdapterTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    GlassRobotAdapter robotAdapter;
    Stage targetStage;
    Parent sceneRoot;
    Region region;
    Point2D regionCenter;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        robotAdapter = new GlassRobotAdapter();
        targetStage = FxToolkit.setupStage(stage -> {
            region = new Region();
            region.setStyle("-fx-background-color: magenta;");

            VBox box = new VBox(region);
            box.setPadding(new Insets(10));
            box.setSpacing(10);
            VBox.setVgrow(region, Priority.ALWAYS);

            sceneRoot = new StackPane(box);
            Scene scene = new Scene(sceneRoot, 300, 100);
            stage.setScene(scene);
            stage.show();
        });

        PointLocator pointLocator = new PointLocatorImpl(new BoundsLocatorImpl());
        regionCenter = pointLocator.point(region).atPosition(Pos.CENTER).query();
    }

    @After
    public void cleanup() {
        robotAdapter.keyRelease(KeyCode.A);
        robotAdapter.mouseRelease(MouseButton.PRIMARY);
    }

    @Test
    public void robotCreate() {
        // when:
        robotAdapter.robotCreate();

        // then:
        assertThat(robotAdapter.getRobotInstance(), notNullValue());
    }

    @Test
    public void robotDestroy_initialized_robot() {
        // given:
        robotAdapter.robotCreate();

        // when:
        robotAdapter.robotDestroy();

        // then:
        assertThat(robotAdapter.getRobotInstance(), nullValue());
    }

    @Test
    public void robotDestroy_uninitialized_robot() {
        // when:
        robotAdapter.robotDestroy();

        // then:
        assertThat(robotAdapter.getRobotInstance(), nullValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void keyPress() {
        // given:
        EventHandler<KeyEvent> keyEventHandler = mock(EventHandler.class);
        targetStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);

        // and:
        robotAdapter.mouseMove(regionCenter);

        // when:
        robotAdapter.keyPress(KeyCode.A);

        // then:
        WaitForAsyncUtils.waitForFxEvents();
        verify(keyEventHandler, times(1)).handle(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void keyRelease() {
        // given:
        EventHandler<KeyEvent> keyEventHandler = mock(EventHandler.class);
        targetStage.addEventHandler(KeyEvent.KEY_RELEASED, keyEventHandler);

        // and:
        robotAdapter.mouseMove(regionCenter);

        // when:
        robotAdapter.keyPress(KeyCode.A);
        robotAdapter.keyRelease(KeyCode.A);

        // then:
        WaitForAsyncUtils.waitForFxEvents();
        verify(keyEventHandler, times(1)).handle(any());
    }

    @Test
    public void getMouseLocation() {
        // when:
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat("mouseLocation.getX() is greater than or equal to 0.0", mouseLocation.getX() >= 0.0);
        assertThat("mouseLocation.getY() is greater than or equal to 0.0", mouseLocation.getY() >= 0.0);
    }

    @Test
    public void mouseMove() {
        assumeThat("skipping: Robot's mouseMove broken on Windows + HiDPI (JDK-8196031)",
                System.getProperty("glass.win.uiScale", "100%"), is(equalTo("100%")));
        // given:
        robotAdapter.mouseMove(new Point2D(100, 200));

        // when:
        WaitForAsyncUtils.waitForFxEvents();
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat(mouseLocation.getX(), is(100.0));
        assertThat(mouseLocation.getY(), is(200.0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mousePress() {
        // given:
        EventHandler<MouseEvent> mouseEventHandler = mock(EventHandler.class);
        region.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEventHandler);

        // and:
        robotAdapter.mouseMove(regionCenter);

        // when:
        robotAdapter.mousePress(MouseButton.PRIMARY);

        // then:
        WaitForAsyncUtils.waitForFxEvents();
        verify(mouseEventHandler, times(1)).handle(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mouseRelease() {
        // given:
        EventHandler<MouseEvent> mouseEventHandler = mock(EventHandler.class);
        region.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEventHandler);

        // and:
        robotAdapter.mouseMove(regionCenter);

        // when:
        robotAdapter.mousePress(MouseButton.PRIMARY);
        robotAdapter.mouseRelease(MouseButton.PRIMARY);

        // then:
        WaitForAsyncUtils.waitForFxEvents();
        verify(mouseEventHandler, times(1)).handle(any());
    }

    @Test
    public void getCapturePixelColor() {
        // given:
        assumeThat(System.getenv("TRAVIS_OS_NAME"), is(not(equalTo("osx"))));
        assumeThat(System.getProperty("prism.order", ""), is(not(equalTo("d3d"))));

        // when:
        Color pixelColor = robotAdapter.getCapturePixelColor(regionCenter);

        // then:
        assertThat(pixelColor, is(Color.web("magenta")));
    }

    @Test
    public void getCaptureRegion() {
        // given:
        assumeThat(System.getenv("TRAVIS_OS_NAME"), is(not(equalTo("osx"))));
        assumeThat(System.getProperty("prism.order", ""), is(not(equalTo("d3d"))));

        // when:
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(region);
        Image regionImage = robotAdapter.getCaptureRegion(new Rectangle2D(bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth(), bounds.getHeight()));

        // then:
        assertThat(regionImage.getPixelReader().getColor((int) regionImage.getWidth() / 2,
                (int) regionImage.getHeight() / 2), is(Color.web("magenta")));
    }

    @Test
    public void timerWaitForIdle() {
        // when:
        AtomicBoolean reachedStatement = new AtomicBoolean(false);
        asyncFx(() -> {
            sleep(100, TimeUnit.MILLISECONDS);
            asyncFx(() -> reachedStatement.set(true));
        });
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(reachedStatement.get(), is(true));
    }

}
