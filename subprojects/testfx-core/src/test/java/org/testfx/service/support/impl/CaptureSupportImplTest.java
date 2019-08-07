/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2019 The TestFX Contributors
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
package org.testfx.service.support.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.testfx.TestFXRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.PixelMatcherResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.testfx.api.FxAssert.verifyThat;

public class CaptureSupportImplTest extends FxRobot {

    @Rule(order = 0)
    public TestRule rule = new TestFXRule();
    @Rule(order = 1)
    public TemporaryFolder testFolder = new TemporaryFolder();

    CaptureSupport capturer;
    Stage primaryStage;

    public static class LoginDialog extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            String fxmlDocument = "acme-login.fxml";
            Node fxmlHierarchy = FXMLLoader.load(getClass().getResource(fxmlDocument));

            Pane rootPane = new StackPane();
            rootPane.getChildren().add(fxmlHierarchy);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Before
    public void setup() throws Exception {
        primaryStage = FxToolkit.registerPrimaryStage();
        capturer = new CaptureSupportImpl(new BaseRobotImpl());
        FxToolkit.setupApplication(LoginDialog.class);
    }

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
        Image image = capturer.loadImage(resourcePath(getClass(), "acme-login-expected.png"));

        // then:
        assertThat(image.getWidth(), equalTo(300.0));
        assertThat(image.getHeight(), equalTo(384.0));
    }

    @Test
    public void save_image() throws IOException {
        // when:
        Image image = capturer.captureNode(primaryStage.getScene().getRoot());
        Path actualImagePath = testFolder.newFile("acme-login-actual.png").toPath();
        capturer.saveImage(image, actualImagePath);

        // then:
        assertThat(actualImagePath.toFile().exists(), is(true));
    }

    @Test
    public void match_images() throws IOException {
        // given:
        Image image0 = capturer.loadImage(resourcePath(getClass(), "acme-login-expected.png"));
        Image image1 = capturer.loadImage(resourcePath(getClass(), "acme-login-actual.png"));

        // when:
        PixelMatcherRgb matcher = new PixelMatcherRgb();
        PixelMatcherResult result = capturer.matchImages(image0, image1, matcher);
        Path differenceImagePath = testFolder.newFile("acme-login-difference.png").toPath();
        capturer.saveImage(result.getMatchImage(), differenceImagePath);

        // then:
        assertThat(differenceImagePath.toFile().exists(), is(true));
        verifyThat(result.getNonMatchPixels(), equalTo(2191L));
        verifyThat(result.getNonMatchFactor(), closeTo(0.02, /* tolerance */ 0.01));
    }

    @Test
    public void match_images_from_scene() throws IOException {
        // given:
        interact(() -> primaryStage.getScene().lookup("#loginButton").requestFocus());
        Image image0 = capturer.captureNode(primaryStage.getScene().getRoot());

        // and:
        interact(() -> primaryStage.getScene().lookup("#username").requestFocus());
        Image image1 = capturer.captureNode(primaryStage.getScene().getRoot());

        // when:
        PixelMatcherRgb matcher = new PixelMatcherRgb();
        PixelMatcherResult result = capturer.matchImages(image0, image1, matcher);
        Path differenceImagePath = testFolder.newFile("acme-login-difference.png").toPath();
        capturer.saveImage(result.getMatchImage(), differenceImagePath);

        // then:
        assertThat(differenceImagePath.toFile().exists(), is(true));
        assertThat(result.getNonMatchPixels() > 1900, is(true));
        verifyThat(result.getNonMatchFactor(), closeTo(0.02, /* tolerance */ 0.01));
    }

    private Path resourcePath(Class<?> contextClass, String resourceName) {
        try {
            URL url = contextClass.getResource(resourceName);
            Objects.requireNonNull(url, "url must not be null");
            return Paths.get(url.toURI());
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
