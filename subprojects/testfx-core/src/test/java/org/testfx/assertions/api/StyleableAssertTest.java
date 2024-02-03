/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.assertions.api;

import javafx.css.Styleable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.testfx.assertions.api.Assertions.assertThat;

public class StyleableAssertTest extends FxRobot {

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void hasId() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setId("myButton");
            return button;
        });

        // then:
        assertThat(styleable).hasId("myButton");
    }

    @Test
    public void doesNotHaveId() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setId("myButton");
            return button;
        });

        // then:
        assertThat(styleable).doesNotHaveId("notMyButton");
    }

    @Test
    public void hasTypeSelector() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> new Button("Button"));

        // then:
        assertThat(styleable).hasTypeSelector("Button");
    }

    @Test
    public void doesNotHaveTypeSelector() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> new Button("Button"));

        // then:
        assertThat(styleable).doesNotHaveTypeSelector("Label");
    }

    @Test
    public void hasStyle() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setStyle("-fx-background-color: RED;");
            return button;
        });

        // then:
        assertThat(styleable).hasStyle("-fx-background-color: RED;");
    }

    @Test
    public void doesNotHaveStyle() throws Exception {
        // given (a Button which is-a Styleable):
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setStyle("-fx-background-color: RED;");
            return button;
        });

        // then:
        assertThat(styleable).doesNotHaveStyle("-fx-background-color: BLUE;");
    }

    @Test
    public void hasStyleableParent() throws Exception {
        // given (a Button which is-a Styleable contained in a StackPane):
        StackPane[] pane = new StackPane[1];
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setStyle("-fx-background-color: RED;");
            StackPane stackPane = new StackPane(button);
            pane[0] = stackPane;
            return button;
        });

        // then:
        assertThat(styleable).hasStyleableParent(pane[0]);
    }

    @Test
    public void doesNotHaveStyleableParent() throws Exception {
        // given (a Button which is-a Styleable contained in a StackPane):
        StackPane[] pane = new StackPane[1];
        Styleable styleable = FxToolkit.setupFixture(() -> {
            Button button = new Button("Button");
            button.setStyle("-fx-background-color: RED;");
            StackPane stackPane = new StackPane();
            pane[0] = stackPane;
            return button;
        });

        // then:
        assertThat(styleable).doesNotHaveStyleableParent(pane[0]);
    }
}
