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
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.service.support.CaptureSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CaptureSupportImplTest {

    public static class LoginDialog extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            Pane rootPane = new StackPane();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

            String fxmlDocument = "res/acmekit-login-flower.fxml";
            Node fxmlHierarchy = FXMLLoader.load(getClass().getResource(fxmlDocument));
            rootPane.getChildren().add(fxmlHierarchy);
            fxmlHierarchy.lookup("#loginButton").requestFocus();

            stage.show();
        }
    }

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    private CaptureSupport captureSupport;

    private Stage primaryStage;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() throws Exception {
        primaryStage = FxToolkit.registerPrimaryStage();
        captureSupport = new CaptureSupportImpl(new BaseRobotImpl());
        FxToolkit.setupApplication(LoginDialog.class);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void capture_region() {
        // when:
        Image image = captureSupport.captureRegion(new Rectangle2D(0, 0, 100, 200));

        // then:
        assertThat(image.getWidth(), equalTo(100.0));
        assertThat(image.getHeight(), equalTo(200.0));
    }

    @Test
    public void capture_node() {
        // when:
        Image image = captureSupport.captureNode(primaryStage.getScene().getRoot());

        // then:
        assertThat(image.getWidth(), equalTo(primaryStage.getScene().getWidth()));
        assertThat(image.getHeight(), equalTo(primaryStage.getScene().getHeight()));
    }

    @Test
    public void load_image() throws Exception {
        //waitFor(99, TimeUnit.MINUTES, () -> !primaryStage.isShowing());
    }

}
