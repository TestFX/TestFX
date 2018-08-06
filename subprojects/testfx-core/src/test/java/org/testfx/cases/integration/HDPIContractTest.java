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
package org.testfx.cases.integration;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.junit.After;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.internal.JavaVersionAdapter;
import org.testfx.internal.PlatformAdapter;
import org.testfx.internal.PlatformAdapter.OS;
import org.testfx.service.adapter.RobotAdapter;
import org.testfx.service.adapter.impl.AwtRobotAdapter;
import org.testfx.service.adapter.impl.GlassRobotAdapter;
import org.testfx.service.locator.impl.BoundsLocatorImpl;
import org.testfx.util.WaitForAsyncUtils;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests the contract for HDPI (and normal resolution).<br>
 * The contract is:
 * <ul>
 * <li>The higher level components work with JavaFx coordinates</li>
 * <li>The RobotAdapter translates to screen coordinates if required</li>
 * </ul>
 *
 */
public class HDPIContractTest extends FxRobot {

    Stage stage;
    Scene scene;
    Pane root;
    Rectangle topLeft;
    Rectangle topRight;
    Rectangle bottomLeft;
    Rectangle bottomRight;

    AwtRobotAdapter awtAdapter;
    GlassRobotAdapter glassAdapter;

    private long waitBetween = 200;

    private final boolean verbose = false;

    @Before
    public void setupHDPI() throws Exception {
        FxToolkit.registerPrimaryStage();

        // Provide some background component for the basic test, that can fetch
        // some key and mouse events.
        // See issue #593 (https://github.com/TestFX/TestFX/issues/593).
        FxToolkit.setupStage(stage -> {
            this.stage = new Stage(); //don't mess up the primary stage!
            this.stage.initStyle(StageStyle.UNDECORATED);
            root = new Pane();
            String bg = "-fx-background-color: magenta;";
            root.setStyle(bg);

            bg = "-fx-background-color: black;";
            topLeft = new Rectangle(0, 0, 5, 5);
            topLeft.setStyle(bg);
            root.getChildren().add(topLeft);

            topRight = new Rectangle(95, 0, 5, 5);
            topRight.setStyle(bg);
            root.getChildren().add(topRight);

            bottomLeft = new Rectangle(0, 95, 5, 5);
            bottomLeft.setStyle(bg);
            root.getChildren().add(bottomLeft);

            bottomRight = new Rectangle(95, 95, 5, 5);
            bottomRight.setStyle(bg);
            root.getChildren().add(bottomRight);

            scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();
        });
        initRobots();
        log("JavaVersion: " + System.getProperty("java.version"));
        log("Java scaling x=" + JavaVersionAdapter.getScreenScaleX() +
                "Java scaling y=" + JavaVersionAdapter.getScreenScaleY());
    }

    @After
    public void tearDownHDPI() throws Throwable {
        interact(() -> stage.close());
        WaitForAsyncUtils.waitForFxEvents();
        WaitForAsyncUtils.checkException();
    }

    public void initRobots() {
        try {
            awtAdapter = new AwtRobotAdapter();
        }
        catch (Exception e) {
            log("AwtRobotAdapter seems to be not supported on this Platform");
        }
        try {
            glassAdapter = GlassRobotAdapter.createGlassRobot();
        }
        catch (Exception e) {
            log("GlassRobotAdapter seems to be not supported on this Platform");
        }
    }

    /**
     * Checks that same location is returned, so if set location works in JavaFx
     * Coordinates, the robot is consistent.
     */
    public void mouseLocationAdapterTest(RobotAdapter<?> adapter) {
        Point2D testPt = new Point2D(100, 100);
        adapter.mouseMove(testPt);
        sleep(waitBetween); // ensure there are no timing issues
        // note: if this test fails in the future on HDPI Unix with Java > 10 see
        // comment in GlassRobotAdapter.getMouseLocation()
        Point2D readPt = adapter.getMouseLocation();
        log("TestMouse for " + adapter.getClass().getSimpleName() + " is " + readPt);
        assertThat("Move and read back failed for " + adapter.getClass().getSimpleName(), readPt, equalTo(testPt));
    }

    @Test
    public void mouseLocationAwtAdapterTest() {
        try {
            mouseLocationAdapterTest(awtAdapter);
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void mouseLocationGlassAdapterTest() {
        try {
            mouseLocationAdapterTest(glassAdapter);
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    /**
     * Checks that returned bounds are in JavaFx coordinates, as a offset of (0,0)
     * is used, the coordinates of the window do not affect the result.<br>
     * If the Robot hits those Points, the Robot is definitely working correct in
     * JavaFx coordinates.
     */
    public void nullOffsetAdapterTest(RobotAdapter<?> adapter) {
        interact(() -> {
            stage.setX(0);
            stage.setY(0);
        });
        sleep(1000);

        // Checking that bounds are in JavaFx-Coordinates
        BoundsLocatorImpl locator = new BoundsLocatorImpl();
        Bounds bounds = locator.boundsOnScreenFor(root);
        log("Top left window bounds for " + adapter.getClass().getSimpleName() + " is " + bounds);

        // checking correct bounds
        // interesting feature on mac:
        // Running in eclipse on mac, the window is placed below the MenuBar
        // running within gradle the window is positioned behind the menubar (at real (0,0) offset)
        assertThat("Min x doesn't match", bounds.getMinX(), equalTo(0.0));
        if (PlatformAdapter.getOs() != OS.MAC) { // Respect control bar on mac (top)
            assertThat("MinY doesn't match", bounds.getMinY(), equalTo(0.0));
        }
        assertThat("Width doesn't match", bounds.getWidth(), equalTo(100.0));
        assertThat("Height doesn't match", bounds.getHeight(), equalTo(100.0));

        // checking Robot clicks on the JavaFx coordinates (if coordinates above are
        // correct)
        EventHandler<MouseEvent> topLeftEvent = mock(EventHandler.class);
        interact(() -> topLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, topLeftEvent));
        EventHandler<MouseEvent> topRightEvent = mock(EventHandler.class);
        interact(() -> topRight.addEventHandler(MouseEvent.MOUSE_PRESSED, topRightEvent));
        EventHandler<MouseEvent> bottomLeftEvent = mock(EventHandler.class);
        interact(() -> bottomLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, bottomLeftEvent));
        EventHandler<MouseEvent> bottomRightEvent = mock(EventHandler.class);
        interact(() -> bottomRight.addEventHandler(MouseEvent.MOUSE_PRESSED, bottomRightEvent));

        // using bounds because of mac menu bar...
        if (PlatformAdapter.getOs() != OS.MAC) { // clicking menus on mac is not cool...
            adapter.mouseMove(new Point2D(bounds.getMinX() + 2.5, bounds.getMinY() + 2.5));
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            adapter.mousePress(MouseButton.PRIMARY);
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            adapter.mouseRelease(MouseButton.PRIMARY);
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            log("TopLeft Click " + adapter.getClass().getSimpleName() + ": " +
                    Mockito.mockingDetails(topLeftEvent).getInvocations().size());

            adapter.mouseMove(new Point2D(bounds.getMaxX() - 2.5, bounds.getMinY() + 2.5));
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            adapter.mousePress(MouseButton.PRIMARY);
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            adapter.mouseRelease(MouseButton.PRIMARY);
            WaitForAsyncUtils.waitForFxEvents();
            sleep(waitBetween); //ensure there are no timing issues
            log("TopRight Click " + adapter.getClass().getSimpleName() + ": " +
                    Mockito.mockingDetails(topRightEvent).getInvocations().size());
        }

        adapter.mouseMove(new Point2D(bounds.getMinX() + 2.5, bounds.getMaxY() - 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("BottomLeft Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(bottomLeftEvent).getInvocations().size());

        adapter.mouseMove(new Point2D(bounds.getMaxX() - 2.5, bounds.getMaxY() - 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("BottomRight Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(bottomRightEvent).getInvocations().size());

        // checking mouse has hit
        if (PlatformAdapter.getOs() != OS.MAC) { // Respect control bar on mac (top)
            verify(topLeftEvent, times(1)).handle(any());
            verify(topRightEvent, times(1)).handle(any());
        }
        verify(bottomLeftEvent, times(1)).handle(any());
        verify(bottomRightEvent, times(1)).handle(any());
    }

    @Test
    public void nullOffsetAwtAdapterTest() {
        try {
            nullOffsetAdapterTest(awtAdapter);
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void nullOffsetGlassAdapterTest() {
        try {
            nullOffsetAdapterTest(glassAdapter);
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    /**
     * The nullOffsetTest has already verified that the Robot uses Screen
     * coordinates and that the BoundsLocator returns JavaFx Coordinates within a
     * JavaFx component. Last thing that might not work, is that the translation of
     * the window is calculated in screen coordinates.<br>
     * So if this test fails (and nullOffset works), the scaling has to be accounted
     * to the translation of the window.
     */
    public void defaultOffsetAdapterTest(RobotAdapter<?> adapter) {
        sleep(1000);

        // Checking that bounds are in JavaFx-Coordinates
        BoundsLocatorImpl locator = new BoundsLocatorImpl();
        Bounds bounds = locator.boundsOnScreenFor(root);
        log("Default window bounds for " + adapter.getClass().getSimpleName() + " is " + bounds);

        // checking correct bounds (x,y) is unknown...
        assertThat("Width doesn't match", bounds.getWidth(), equalTo(100.0));
        assertThat("Height doesn't match", bounds.getHeight(), equalTo(100.0));

        // using robot to verify the coordinates
        EventHandler<MouseEvent> topLeftEvent = mock(EventHandler.class);
        interact(() -> topLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, topLeftEvent));
        EventHandler<MouseEvent> topRightEvent = mock(EventHandler.class);
        interact(() -> topRight.addEventHandler(MouseEvent.MOUSE_PRESSED, topRightEvent));
        EventHandler<MouseEvent> bottomLeftEvent = mock(EventHandler.class);
        interact(() -> bottomLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, bottomLeftEvent));
        EventHandler<MouseEvent> bottomRightEvent = mock(EventHandler.class);
        interact(() -> bottomRight.addEventHandler(MouseEvent.MOUSE_PRESSED, bottomRightEvent));

        adapter.mouseMove(new Point2D(bounds.getMinX() + 2.5, bounds.getMinY() + 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("TopLeft Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(topLeftEvent).getInvocations().size());

        adapter.mouseMove(new Point2D(bounds.getMaxX() - 2.5, bounds.getMinY() + 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("TopRight Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(topRightEvent).getInvocations().size());

        adapter.mouseMove(new Point2D(bounds.getMinX() + 2.5, bounds.getMaxY() - 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("BottomLeft Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(bottomLeftEvent).getInvocations().size());

        adapter.mouseMove(new Point2D(bounds.getMaxX() - 2.5, bounds.getMaxY() - 2.5));
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        log("BottomRight Click " + adapter.getClass().getSimpleName() + ": " +
                Mockito.mockingDetails(bottomRightEvent).getInvocations().size());

        // checking mouse has hit
        verify(topLeftEvent, times(1)).handle(any());
        verify(topRightEvent, times(1)).handle(any());
        verify(bottomLeftEvent, times(1)).handle(any());
        verify(bottomRightEvent, times(1)).handle(any());
    }

    @Test
    public void defaultOffsetAwtAdapterTest() {
        try {
            defaultOffsetAdapterTest(awtAdapter);
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void defaultOffsetGlassAdapterTest() {
        try {
            defaultOffsetAdapterTest(glassAdapter);
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    /**
     * Just verifies, that grabPixelColor also uses the right coordinates.
     * defaultOffsetTest must work. Does only a approximate test. Uses Black, to
     * minimize error in case of color profiles. And uses a high threshold.
     */
    public void capturePixelColorAdapterTest(RobotAdapter<?> adapter) {
        sleep(1000);

        // Checking that bounds are in JavaFx-Coordinates
        BoundsLocatorImpl locator = new BoundsLocatorImpl();
        Bounds bounds = locator.boundsOnScreenFor(root);
        log("CapturePixel window bounds for " + adapter.getClass().getSimpleName() + " is " + bounds);

        // checking correct bounds (x,y) is unknown...
        assertThat("Width doesn't match", bounds.getWidth(), equalTo(100.0));
        assertThat("Height doesn't match", bounds.getHeight(), equalTo(100.0));


        Color c = adapter.getCapturePixelColor(new Point2D(bounds.getMinX() + 2.5, bounds.getMinY() + 2.5));
        assertTrue("TopLeft color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("TopLeft color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("TopLeft color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = adapter.getCapturePixelColor(new Point2D(bounds.getMaxX() - 2.5, bounds.getMinY() + 2.5));
        assertTrue("TopRight color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("TopRight color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("TopRight color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = adapter.getCapturePixelColor(new Point2D(bounds.getMinX() + 2.5, bounds.getMaxY() - 2.5));
        assertTrue("BottomLeft color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("BottomLeft color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("BottomLeft color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = adapter.getCapturePixelColor(new Point2D(bounds.getMaxX() - 2.5, bounds.getMaxY() - 2.5));
        assertTrue("BottomRight color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("BottomRight color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("BottomRight color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

    }

    @Test
    public void capturePixelColorAwtAdapterTest() {
        try {
            capturePixelColorAdapterTest(awtAdapter);
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void capturePixelColorGlassAdapterTest() {
        try {
            capturePixelColorAdapterTest(glassAdapter);
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    /**
     * Just verifies, that grabPixelColor also uses the right coordinates.
     * defaultOffsetTest must work. Does only a approximate test. Uses Black, to
     * minimize error in case of color profiles. And uses a high threshold.
     */
    public void captureRegionAdapterTest(RobotAdapter<?> adapter) {
        sleep(1000);

        // Checking that bounds are in JavaFx-Coordinates
        BoundsLocatorImpl locator = new BoundsLocatorImpl();
        Bounds bounds = locator.boundsOnScreenFor(root);
        log("CaptureRegion window bounds for " + adapter.getClass().getSimpleName() + " is " + bounds);

        // checking correct bounds (x,y) is unknown...
        assertThat("Width doesn't match", bounds.getWidth(), equalTo(100.0));
        assertThat("Height doesn't match", bounds.getHeight(), equalTo(100.0));

        // not really required, as Robot coordinates are verified in nullOffsetTest, but
        // for completeness...
        // checking Robot clicks on the JavaFx coordinates (if coordinates above are
        // correct)
        Image image = adapter.getCaptureRegion(
                new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        PixelReader img = image.getPixelReader();
        // verify correctness of dimensions
        assertTrue("Width of image incorrect. Is=" + image.getWidth() + " expected " + bounds.getWidth(),
                Math.abs(image.getWidth() - bounds.getWidth()) < 1.0);
        assertTrue("Height of image incorrect. Is=" + image.getHeight() + " expected " + bounds.getHeight(),
                Math.abs(image.getHeight() - bounds.getHeight()) < 1.0);

        // verify correctness of content
        Color c = img.getColor(2, 2);
        assertTrue("TopLeft color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("TopLeft color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("TopLeft color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = img.getColor((int) bounds.getWidth() - 2, 2);
        assertTrue("TopRight color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("TopRight color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("TopRight color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = img.getColor(2, (int) bounds.getHeight() - 2);
        assertTrue("BottomLeft color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("BottomLeft color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("BottomLeft color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

        c = img.getColor((int) bounds.getWidth() - 2, (int) bounds.getHeight() - 2);
        assertTrue("BottomRight color doesn't match " + c, c.getRed() < 16.0 / 255.0);
        assertTrue("BottomRight color doesn't match " + c, c.getGreen() < 16.0 / 255.0);
        assertTrue("BottomRight color doesn't match " + c, c.getBlue() < 16.0 / 255.0);

    }

    @Test
    public void captureRegionAwtAdapterTest() {
        try {
            captureRegionAdapterTest(awtAdapter);
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void captureRegionGlassAdapterTest() {
        try {
            captureRegionAdapterTest(glassAdapter);
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }


    //// Just debugging: ////

    /**
     * Just another test, that double verifies the Robot handler coordinates...
     */
    public void clickOffsetAdapterTest(RobotAdapter<?> adapter) {
        interact(() -> {
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(400);
            stage.setHeight(400);
        });
        interact(() -> root.getChildren().clear());
        sleep(1000);

        // Checking that bounds are in JavaFx-Coordinates
        BoundsLocatorImpl locator = new BoundsLocatorImpl();
        Bounds bounds = locator.boundsOnScreenFor(root);
        log(awtAdapter.getClass().getSimpleName() + ": Click offset window bounds for " +
                adapter.getClass().getSimpleName() + " is " + bounds);

        assumeThat("If the bounds have an offset at the top left edge, this test provides no additional information",
                bounds.getMinY(), equalTo(0.0));


        // checking Robot clicks on the JavaFx coordinates (if coordinates above are
        // correct)
        EventHandler<MouseEvent> rootEvent = mock(EventHandler.class);
        interact(() -> root.addEventHandler(MouseEvent.MOUSE_PRESSED, rootEvent));

        // using bounds because of mac menu bar...
        adapter.mouseMove(new Point2D(50, 50)); //will work up to a scaling factor of 8x
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mousePress(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues
        adapter.mouseRelease(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(waitBetween); //ensure there are no timing issues


        // checking mouse has hit
        ArgumentCaptor<MouseEvent> ev = ArgumentCaptor.forClass(MouseEvent.class);
        verify(rootEvent, times(1)).handle(ev.capture()); //can't output coordinates
        // if hit check
        MouseEvent mEv = ev.getAllValues().get(0);
        log(awtAdapter.getClass().getSimpleName() + ": ClickOffset landed on " +
                adapter.getClass().getSimpleName() + ": " + Mockito.mockingDetails(rootEvent).printInvocations());
        assertTrue(awtAdapter.getClass().getSimpleName() + ": Expected click to be at x 50.0, but was at " + mEv.getX(),
                Math.abs(mEv.getX() - 50.0) < 1.0);
        assertTrue(awtAdapter.getClass().getSimpleName() + ": Expected click to be at y 50.0, but was at " + mEv.getY(),
                Math.abs(mEv.getY() - 50.0) < 1.0);
    }

    @Test
    public void clickOffsetAwtAdapterTest() {
        try {
            clickOffsetAdapterTest(awtAdapter);
        }
        catch (AssumptionViolatedException e) {
            throw e;
        }
        catch (Exception e) {
            log(awtAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }

    @Test
    public void clickOffsetGlassAdapterTest() {
        try {
            clickOffsetAdapterTest(glassAdapter);
        }
        catch (AssumptionViolatedException e) {
            throw e;
        }
        catch (Exception e) {
            log(glassAdapter.getClass().getSimpleName() + " seems to be not supported on this Platform");
        }
    }


    protected void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }

}
