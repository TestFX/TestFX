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
package org.testfx.matcher.base;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class NodeMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule();

    TextField textField;
    TextField textField2;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void anything() throws Exception {
        List<Node> nodes = FxToolkit.setupFixture(() -> {
            List<Node> temp = new ArrayList<>(3);
            temp.add(new Region());
            temp.add(new Button("foo"));
            temp.add(new TextField("bar"));
            return temp;
        });

        assertThat(from(nodes).match(NodeMatchers.anything()).queryAll(), hasItem(nodes.get(1)));
    }

    @Test
    public void isFocused() throws Exception {
        // given:
        FxToolkit.setupSceneRoot(() -> {
            textField = new TextField("foo");
            return new StackPane(textField);
        });

        // when:
        Platform.runLater(() -> textField.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();


        // then:
        assertThat(textField, NodeMatchers.isFocused());
    }

    @Test
    public void isFocused_fails() throws Exception {
        // given:
        FxToolkit.setupSceneRoot(() -> {
            textField = new TextField("foo");
            textField2 = new TextField("bar");
            return new StackPane(textField, textField2);
        });

        // when:
        Platform.runLater(() -> textField2.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(textField, NodeMatchers.isFocused()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node has focus\n");
    }

    @Test
    public void isNotFocused() throws Exception {
        // given:
        FxToolkit.setupSceneRoot(() -> {
            textField = new TextField("foo");
            textField2 = new TextField("bar");
            return new StackPane(textField, textField2);
        });

        Platform.runLater(() -> textField2.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(textField, NodeMatchers.isNotFocused());
    }

    @Test
    public void isNotFocused_fails() throws Exception {
        // given:
        FxToolkit.setupSceneRoot(() -> {
            textField = new TextField("foo");
            return new StackPane(textField);
        });

        // when:
        Platform.runLater(() -> textField.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(textField, NodeMatchers.isNotFocused()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node does not have focus\n");
    }

    @Test
    public void hasText_filters_nodes() throws Exception {
        // given:
        List<Node> nodes =  FxToolkit.setupFixture(() -> {
            List<Node> temp = new ArrayList<>(3);
            temp.add(new Region());
            temp.add(new Button("foo"));
            temp.add(new TextField("bar"));
            return temp;
        });

        // then:
        NodeQuery query1 = from(nodes).match(LabeledMatchers.hasText("foo"));
        assertThat(query1.queryAll(), hasItems(nodes.get(1)));

        NodeQuery query2 = from(nodes).match(TextInputControlMatchers.hasText("bar"));
        assertThat(query2.queryAll(), hasItems(nodes.get(2)));
    }

    @Test
    public void hasChild() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent, NodeMatchers.hasChild(".button"));
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane());

        // then:
        assertThatThrownBy(() -> assertThat(parent, NodeMatchers.hasChild(".button")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node has child \".button\"\n");
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent, NodeMatchers.hasChildren(2, ".button"));
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(new Label("foo"), new Button("bar")));

        // then:
        assertThatThrownBy(() -> assertThat(parent, NodeMatchers.hasChildren(2, ".button")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node has 2 children \".button\"\n");
    }

}
