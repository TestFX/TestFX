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

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.service.support.CaptureSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CaptureSupportImplTest {


    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    private CaptureSupport captureSupport;

    private Rectangle rectangle;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        captureSupport = new CaptureSupportImpl(new BaseRobotImpl());
        FxToolkit.setupScene(() -> {
            StackPane root = new StackPane();
            Scene scene = new Scene(root, 400, 400);
            rectangle = new Rectangle(0, 0, 100, 200);
            root.getChildren().add(rectangle);
            return scene;
        });
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
        Image image = captureSupport.captureNode(rectangle);

        // then:
        assertThat(image.getWidth(), equalTo(100.0));
        assertThat(image.getHeight(), equalTo(200.0));
    }

}
