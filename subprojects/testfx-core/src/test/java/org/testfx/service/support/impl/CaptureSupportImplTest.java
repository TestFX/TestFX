/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.support.impl;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.PixelMatcherResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.testfx.api.FxAssert.verifyThat;

public class CaptureSupportImplTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIXTURES.
    //---------------------------------------------------------------------------------------------

    private CaptureSupport capturer;

    private Stage primaryStage;

    public static class LoginDialog extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            String fxmlDocument = "res/acme-login.fxml";
            Node fxmlHierarchy = FXMLLoader.load(getClass().getResource(fxmlDocument));

            Pane rootPane = new StackPane();
            rootPane.getChildren().add(fxmlHierarchy);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.show();
        }
    }

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() throws Exception {
        primaryStage = FxToolkit.registerPrimaryStage();
        capturer = new CaptureSupportImpl(new BaseRobotImpl());
        FxToolkit.setupApplication(LoginDialog.class);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void capture_node() {
        // when:
        Image image = capturer.captureNode(primaryStage.getScene().getRoot());

        // then:
        assertThat(image.getWidth(), equalTo(primaryStage.getScene().getWidth()));
        assertThat(image.getHeight(), equalTo(primaryStage.getScene().getHeight()));
    }

    @Test
    public void capture_region() {
        // when:
        Image image = capturer.captureRegion(new Rectangle2D(0, 0, 100, 200));

        // then:
        assertThat(image.getWidth(), equalTo(100.0));
        assertThat(image.getHeight(), equalTo(200.0));
    }

    @Test
    public void load_image() {
        // when:
        Image image = capturer.loadImage(resourcePath(getClass(), "res/acme-login-expected.png"));

        // then:
        assertThat(image.getWidth(), equalTo(300.0));
        assertThat(image.getHeight(), equalTo(384.0));
    }

    @Test
    public void save_image() {
        Image image = capturer.captureNode(primaryStage.getScene().getRoot());
//        Files.createTempDir();
//        capturer.saveImage(image, Paths.get("acme-login-actual.png"));
//        waitFor(99, TimeUnit.MINUTES, () -> !primaryStage.isShowing());
    }

    @Test
    public void match_images() {
        // given:
        Image image0 = capturer.loadImage(resourcePath(getClass(), "res/acme-login-expected.png"));
        Image image1 = capturer.loadImage(resourcePath(getClass(), "res/acme-login-actual.png"));

        // when:
        PixelMatcherRgb matcher = new PixelMatcherRgb();
        PixelMatcherResult result = capturer.matchImages(image0, image1, matcher);
//        capturer.saveImage(result.getMatchImage(), Paths.get("acme-login-difference.png"));

        // then:
        verifyThat(result.getNonMatchPixels(), equalTo(2191L));
        verifyThat(result.getNonMatchFactor(), closeTo(0.02, /* tolerance */ 0.01));
    }

    @Test
    @Ignore
    public void match_images_from_scene() {
        // given:
        interact(() -> primaryStage.getScene().lookup("#loginButton").requestFocus());
        Image image0 = capturer.captureNode(primaryStage.getScene().getRoot());

        // and:
        interact(() -> primaryStage.getScene().lookup("#username").requestFocus());
        Image image1 = capturer.captureNode(primaryStage.getScene().getRoot());

        // when:
        PixelMatcherRgb matcher = new PixelMatcherRgb();
        PixelMatcherResult result = capturer.matchImages(image0, image1, matcher);
//        capturer.saveImage(result.getMatchImage(), Paths.get("acme-login-difference.png"));

        // then:
        verifyThat(result.getNonMatchPixels(), equalTo(2191L));
        verifyThat(result.getNonMatchFactor(), closeTo(0.02, /* tolerance */ 0.01));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private Path resourcePath(Class<?> contextClass,
                              String resourceName) {
        try {
            return Paths.get(Resources.getResource(contextClass, resourceName).toURI());
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String generateCaptureFilename(ZonedDateTime dateTime,
                                           String dateTimePattern,
                                           ZoneId zoneId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.withZoneSameInstant(zoneId).format(formatter);
    }

    private ZonedDateTime toZuluTime(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneId.of(/* UTC */ "Z"));
    }

}
