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
package org.testfx.cases.acceptance;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class NodeAndPointQueryTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public FxRobot fx = new FxRobot();

    public Button button0;
    public Button button1;
    public Label label0;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage(stage -> {
            button0 = new Button("click me!");
            button0.setOnAction((actionEvent) -> {
                button0.setText("clicked!");
            });
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

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

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
        assertThat(query.queryAll(), contains(button0, button1));
    }

    @Test
    public void node_queryFirst_returns_null() {
        // when:
        NodeQuery query = fx.lookup(".missing");

        // then:
        assertThat(query.query(), nullValue());
    }

    @Test
    public void node_queryAll_returns_empty_set() {
        // when:
        NodeQuery query = fx.lookup(".missing");

        // then:
        assertThat(query.queryAll(), is(ImmutableSet.of()));
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
        // expect:
        thrown.expect(FxRobotException.class);
        thrown.expectMessage("the query \".missing\" returned no nodes.");

        fx.point(".missing");
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
        // expect:
        thrown.expect(FxRobotException.class);
        thrown.expectMessage("the query \".missing\" returned no nodes.");

        fx.moveTo(".missing").clickOn();
    }

    @Test
    public void moveTo_throws_exception_2() {
        // expect:
        thrown.expect(FxRobotException.class);
        thrown.expectMessage("the query \".label\" returned 1 nodes, but no nodes were visible.");

        fx.moveTo(".label").clickOn();
    }

}
