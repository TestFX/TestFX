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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.PixelMatcherResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testfx.api.FxAssert.verifyThat;

public class CaptureSupportImplTest extends FxRobot {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(testFolder = new TemporaryFolder());
    public TemporaryFolder testFolder;

    CaptureSupport capturer;
    Stage primaryStage;

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
        Image image = capturer.loadImage(resourcePath(getClass(), "res/acme-login-expected.png"));

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
        Image image0 = capturer.loadImage(resourcePath(getClass(), "res/acme-login-expected.png"));
        Image image1 = capturer.loadImage(resourcePath(getClass(), "res/acme-login-actual.png"));

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

    /*
     * Note: Taken from hamcrest-library:
     * https://github.com/hamcrest/JavaHamcrest/tree/master/hamcrest-library
     * <p>
     * This portion of code is Copyright (c) 2000-2015 www.hamcrest.org and BSD
     * licensed: https://github.com/hamcrest/JavaHamcrest/blob/master/LICENSE.txt
     */

    /**
     * Creates a matcher of {@link Double}s that matches when an examined double is equal
     * to the specified <code>operand</code>, within a range of +/- <code>error</code>.
     * For example:
     * <pre>assertThat(1.03, is(closeTo(1.0, 0.03)))</pre>
     *
     * @param operand
     *     the expected value of matching doubles
     * @param error
     *     the delta (+/-) within which matches will be allowed
     */
    public static Matcher<Double> closeTo(double operand, double error) {
        return new IsCloseTo(operand, error);
    }

    /**
     * Is the value a number equal to a value within some range of
     * acceptable error?
     *
     */
    static class IsCloseTo extends TypeSafeMatcher<Double> {
        private final double delta;
        private final double value;

        private IsCloseTo(double value, double error) {
            this.delta = error;
            this.value = value;
        }

        @Override
        public boolean matchesSafely(Double item) {
            return actualDelta(item) <= 0.0;
        }

        @Override
        public void describeMismatchSafely(Double item, Description mismatchDescription) {
            mismatchDescription.appendValue(item)
                    .appendText(" differed by ")
                    .appendValue(actualDelta(item))
                    .appendText(" more than delta ")
                    .appendValue(delta);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a numeric value within ")
                    .appendValue(delta)
                    .appendText(" of ")
                    .appendValue(value);
        }

        private double actualDelta(Double item) {
            return Math.abs(item - value) - delta;
        }
    }
}
