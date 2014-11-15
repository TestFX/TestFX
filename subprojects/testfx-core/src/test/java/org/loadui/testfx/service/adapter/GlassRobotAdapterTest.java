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
package org.loadui.testfx.service.adapter;

import java.util.concurrent.TimeoutException;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.impl.BoundsLocatorImpl;
import org.loadui.testfx.service.locator.impl.PointLocatorImpl;
import org.testfx.api.FxLifecycle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.loadui.testfx.utils.WaitForAsyncUtils.waitForFxEvents;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GlassRobotAdapterTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public GlassRobotAdapter robotAdapter;

    public Stage targetStage;
    public Region region;
    public Parent sceneRoot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws TimeoutException {
        FxLifecycle.registerPrimaryStage();
    }

    @Before
    public void setup() throws TimeoutException {
        robotAdapter = new GlassRobotAdapter();
        targetStage = FxLifecycle.setupStage(stage -> {
            region = new Region();
            sceneRoot = new StackPane(region);
            Scene scene = new Scene(sceneRoot, 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void robotCreate() {
        robotAdapter.robotCreate();
    }

    @Test
    public void getMouseLocation() {
        // when:
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat(mouseLocation.getX(), Matchers.is(greaterThanOrEqualTo(0.0)));
        assertThat(mouseLocation.getY(), Matchers.is(greaterThanOrEqualTo(0.0)));
    }

    @Test
    public void mouseMove() {
        // given:
        robotAdapter.mouseMove(new Point2D(100, 200));

        // when:
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat(mouseLocation.getX(), Matchers.is(100.0));
        assertThat(mouseLocation.getY(), Matchers.is(200.0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mousePress() {
        // given:
        EventHandler<MouseEvent> mouseEventHandler = mock(EventHandler.class);
        region.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEventHandler);

        // and:
        PointLocator pointLocator = new PointLocatorImpl(new BoundsLocatorImpl());
        Point2D point = pointLocator.pointFor(region).atPosition(Pos.CENTER).query();
        robotAdapter.mouseMove(point);

        // when:
        robotAdapter.mousePress(MouseButton.PRIMARY);

        // then:
        waitForFxEvents();
        verify(mouseEventHandler, times(1)).handle(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mouseRelease() {
        // given:
        EventHandler<MouseEvent> mouseEventHandler = mock(EventHandler.class);
        region.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEventHandler);

        // and:
        PointLocator pointLocator = new PointLocatorImpl(new BoundsLocatorImpl());
        Point2D point = pointLocator.pointFor(region).atPosition(Pos.CENTER).query();
        robotAdapter.mouseMove(point);

        // when:
        robotAdapter.mousePress(MouseButton.PRIMARY);
        robotAdapter.mouseRelease(MouseButton.PRIMARY);

        // then:
        waitForFxEvents();
        verify(mouseEventHandler, times(1)).handle(any());
    }

}
