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


import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Robot;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

public class MouseWheelTest extends TestCaseBase {

    static {
        System.setProperty("testfx.robot", "glass");
    }

    @Rule
    public TestFXRule testFXRule = new TestFXRule();
    CountDownLatch setSceneLatch = new CountDownLatch(1);
    CountDownLatch clickLatch = new CountDownLatch(1);
    CountDownLatch scrollTowardsLatchGlass = new CountDownLatch(1);
    CountDownLatch scrollTowardsLatchGlassCheck = new CountDownLatch(1);
    CountDownLatch scrollAwayLatchGlass = new CountDownLatch(1);
    CountDownLatch scrollAwayLatchGlassCheck = new CountDownLatch(1);
    CountDownLatch scrollTowardsLatchAwt = new CountDownLatch(1);
    CountDownLatch scrollTowardsLatchAwtCheck = new CountDownLatch(1);
    CountDownLatch scrollAwayLatchAwt = new CountDownLatch(1);
    CountDownLatch scrollAwayLatchAwtCheck = new CountDownLatch(1);

    FxRobot glassRobot = new FxRobot();
    Robot awtRobot;

    Stage stage;
    Scene scene;
    Rectangle rect;
    ScrollPane scrollPane;
    volatile double oldValue;
    volatile double newValue;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        awtRobot = new Robot();
        InvalidationListener invalidationListener = observable -> setSceneLatch.countDown();
        FxToolkit.setupStage(stage -> {
            this.stage = stage;
            rect = new Rectangle(200, 200, Color.RED);
            rect.setId("rect");
            scrollPane = new ScrollPane();
            scrollPane.setPrefSize(100, 100);
            scrollPane.setContent(rect);
            StackPane pane = new StackPane(new VBox(scrollPane));
            pane.setAlignment(Pos.CENTER);
            scene = new Scene(pane, 300, 400);
            stage.sceneProperty().addListener(observable -> {
                setSceneLatch.countDown();
                stage.sceneProperty().removeListener(invalidationListener);
            });
            stage.setScene(scene);
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void shouldScrollInCorrectDirection() throws InterruptedException {
        WaitForAsyncUtils.waitForFxEvents();
        setSceneLatch.await(10, TimeUnit.SECONDS);
        ScrollBar verticalBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
        verticalBar.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.oldValue = oldValue.doubleValue();
            this.newValue = newValue.doubleValue();
        });
        Platform.runLater(() -> {
            glassRobot.moveTo(point("#rect").atOffset(0, -20));
            glassRobot.press(MouseButton.PRIMARY);
            stage.toFront();
            scrollPane.requestFocus();
            clickLatch.countDown();
        });
        clickLatch.await(5, TimeUnit.SECONDS);

        // Scroll 10 units towards the user using glass robot.
        Platform.runLater(() -> {
            glassRobot.robotContext().getMouseRobot().scroll(10);
            scrollTowardsLatchGlass.countDown();
        });
        scrollTowardsLatchGlass.await(5, TimeUnit.SECONDS);

        // Check that the scrollbar has moved down.
        Platform.runLater(() -> {
            assertThat(newValue).isGreaterThan(0);
            scrollTowardsLatchGlassCheck.countDown();
        });
        scrollTowardsLatchGlassCheck.await(5, TimeUnit.SECONDS);

        // Scroll 10 units away from the user using glass robot.
        Platform.runLater(() -> {
            glassRobot.robotContext().getMouseRobot().scroll(-10);
            scrollAwayLatchGlass.countDown();
        });
        scrollAwayLatchGlass.await(5, TimeUnit.SECONDS);

        // Check that the scrollbar moved back up to 0.
        Platform.runLater(() -> {
            assertThat(newValue).isEqualTo(0);
            scrollAwayLatchGlassCheck.countDown();
        });
        scrollAwayLatchGlassCheck.await(5, TimeUnit.SECONDS);

        // Scroll 10 units towards the user using AWT robot.
        Platform.runLater(() -> {
            awtRobot.mouseWheel(10);
            scrollTowardsLatchAwt.countDown();
        });
        scrollTowardsLatchAwt.await(5, TimeUnit.SECONDS);

        // Check that the scrollbar has moved down.
        Platform.runLater(() -> {
            assertThat(newValue).isGreaterThan(0);
            scrollTowardsLatchAwtCheck.countDown();
        });
        scrollTowardsLatchAwtCheck.await(5, TimeUnit.SECONDS);

        // Scroll 10 units away from the user using AWT robot.
        Platform.runLater(() -> {
            awtRobot.mouseWheel(-10);
            scrollAwayLatchAwt.countDown();
        });
        scrollAwayLatchAwt.await(5, TimeUnit.SECONDS);

        // Check that the scrollbar moved back up to 0.
        Platform.runLater(() -> {
            assertThat(newValue).isEqualTo(0);
            scrollAwayLatchAwtCheck.countDown();
        });
        scrollAwayLatchAwtCheck.await(5, TimeUnit.SECONDS);
        Thread.sleep(5000);
    }


}
