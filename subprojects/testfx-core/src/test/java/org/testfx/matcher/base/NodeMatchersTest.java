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
package org.testfx.matcher.base;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import com.google.common.collect.ImmutableList;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.service.query.NodeQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;

public class NodeMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void anything() throws Exception {
        List<Node> nodes = FxToolkit.setupFixture(() -> {
            return ImmutableList.of(new Region(), new Button("foo"), new TextField("bar"));
        });

        // expect:
        assertThat(from(nodes).select(NodeMatchers.anything()).queryAll(),
            hasItem(NodeMatchers.hasText("bar")));
    }

    @Test
    public void hasText_with_button() throws Exception {
        // given:
        Button button = FxToolkit.setupFixture(() -> new Button("foo"));

        // expect:
        assertThat(button, NodeMatchers.hasText("foo"));
    }

    @Test
    public void hasText_with_text_field() throws Exception {
        // given:
        TextField textField = FxToolkit.setupFixture(() -> new TextField("foo"));

        // expect:
        assertThat(textField, NodeMatchers.hasText("foo"));
    }

    @Test
    public void hasText_with_text() throws Exception {
        // given:
        Text textShape = FxToolkit.setupFixture(() -> new Text("foo"));

        // expect:
        assertThat(textShape, NodeMatchers.hasText("foo"));
    }

    @Test
    public void hasText_with_region_fails() throws Exception {
        // given:
        Region region = FxToolkit.setupFixture(() -> new Region());

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Node has text \"foo\"\n");

        assertThat(region, NodeMatchers.hasText("foo"));
    }

    @Test
    public void hasText_filters_nodes() throws Exception {
        // given:
        List<Node> nodes = FxToolkit.setupFixture(() -> {
            return ImmutableList.of(new Region(), new Button("foo"), new TextField("bar"));
        });

        // expect:
        NodeQuery query1 = from(nodes).select(NodeMatchers.hasText("foo"));
        assertThat(query1.queryAll(), contains(nodes.get(1)));

        // and:
        NodeQuery query2 = from(nodes).select(NodeMatchers.hasText("bar"));
        assertThat(query2.queryAll(), contains(nodes.get(2)));
    }

    @Test
    public void hasChild() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> {
            return new StackPane(new Label("foo"), new Button("bar"), new Button("baz"));
        });

        // expect:
        assertThat(parent, NodeMatchers.hasChild(".button"));
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> {
            return new StackPane();
        });

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Node has child \".button\"\n");

        assertThat(parent, NodeMatchers.hasChild(".button"));
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> {
            return new StackPane(new Label("foo"), new Button("bar"), new Button("baz"));
        });

        // expect:
        assertThat(parent, NodeMatchers.hasChildren(2, ".button"));
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> {
            return new StackPane(new Label("foo"), new Button("bar"));
        });

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Node has 2 children \".button\"\n");

        assertThat(parent, NodeMatchers.hasChildren(2, ".button"));
    }

}
