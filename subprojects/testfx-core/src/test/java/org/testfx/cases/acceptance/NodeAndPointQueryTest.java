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
package org.testfx.cases.acceptance;

import java.util.Collections;
import java.util.HashSet;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NodeAndPointQueryTest {

    @Rule
    public TestRule rule = new TestFXRule();

    FxRobot fx = new FxRobot();
    Button button0;
    Button button1;
    Label label0;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage(stage -> {
            button0 = new Button("click me!");
            button0.setOnAction(actionEvent -> button0.setText("clicked!"));
            button1 = new Button("button");
            label0 = new Label("label");
            label0.setVisible(false);
            StackPane pane = new StackPane(new VBox(button0, button1, label0));
            pane.setAlignment(Pos.CENTER);
            Scene scene = new Scene(pane, 300, 300);
            stage.setScene(scene);
            stage.show();
        });
    }

    @Test
    public void node_queryFirst_returns_node() {
        // when:
        NodeQuery query = fx.lookup(".button");

        // then:
        assertThat(query.query(), is(button0));
    }

    @Test
    public void node_queryAll_returns_set_with_nodes() {
        // when:
        NodeQuery query = fx.lookup(".button");

        // then:
        assertThat(query.queryAll(), hasItems(button0, button1));
    }

    @Test
    public void node_queryFirst_empty_query_throws_exception() {
        // when:
        NodeQuery query = fx.lookup(".missing");

        // then:
        assertThatThrownBy(query::query).isExactlyInstanceOf(EmptyNodeQueryException.class);
    }

    @Test
    public void node_queryAll_returns_empty_set() {
        // when:
        NodeQuery query = fx.lookup(".missing");

        // then:
        assertThat(query.queryAll(), is(Collections.unmodifiableSet(new HashSet<>())));
    }

    @Test
    public void point_query_returns_point() {
        // when:
        PointQuery pointQuery = fx.point(".button");

        // then:
        assertThat(pointQuery.query(), instanceOf(Point2D.class));
    }

    @Test
    public void point_query_throws_exception() {
        assertThatThrownBy(() -> fx.point(".missing"))
                .isExactlyInstanceOf(FxRobotException.class)
                .hasMessage("the query \".missing\" returned no nodes.");
    }

    @Test
    @Ignore("flaky")
    public void moveTo() {
        // when:
        fx.moveTo(".button").clickOn();

        // then:
        assertThat(button0.getText(), is("clicked!"));
    }

    @Test
    public void moveTo_throws_exception() {
        assertThatThrownBy(() -> fx.moveTo(".missing").clickOn())
                .isExactlyInstanceOf(FxRobotException.class)
                .hasMessage("the query \".missing\" returned no nodes.");
    }

    @Test
    public void moveTo_throws_exception_2() {
        assertThatThrownBy(() -> fx.moveTo(".label").clickOn())
                .isExactlyInstanceOf(FxRobotException.class)
                .hasMessage("the query \".label\" returned 1 nodes, but no nodes were visible.");
    }

}
