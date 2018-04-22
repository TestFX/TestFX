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
package org.testfx.cases.issue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;

public class Issue561 extends ApplicationTest {

    Rectangle rectangle;
    Scene scene;
    double mouseX;
    double mouseY;

    @Override
    public void start(Stage stage) {
        rectangle = new Rectangle(100, 100);
        rectangle.setOnMouseClicked(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            System.out.println("mouse clicked on " + mouseX + "," + mouseY);
        });
        scene = new Scene(new Pane(rectangle));
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Test
    public void should_set_point_position() {
        robotContext().setPointPosition(Pos.CENTER);
        moveTo(rectangle).press(MouseButton.PRIMARY);
        robotContext().setPointPosition(Pos.BOTTOM_CENTER);
        assertEquals(50, mouseX, 1.0);
        assertEquals(50, mouseY, 1.0);
        moveTo(rectangle).moveBy(0, -1).clickOn(MouseButton.PRIMARY);
        assertEquals(50, mouseX, 1.0);
        assertEquals(99, mouseY, 1.0);
    }

}
