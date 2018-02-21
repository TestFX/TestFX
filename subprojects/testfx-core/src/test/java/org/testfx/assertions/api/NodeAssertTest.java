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
package org.testfx.assertions.api;

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
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class NodeAssertTest extends FxRobot {

    TextField textField;
    TextField textField2;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void hasText_with_button() throws Exception {
        // given:
        Button button = FxToolkit.setupFixture(() -> new Button("foo"));

        // then:
        assertThat(button).hasText("foo");
    }

    @Test
    public void doesNotHaveText_with_button() throws Exception {
        // given:
        Button button = FxToolkit.setupFixture(() -> new Button("foo"));

        // then:
        assertThat(button).doesNotHaveText("bar");
    }

    @Test
    public void doesNotHaveText_with_button_fails() throws Exception {
        // given:
        Button button = FxToolkit.setupFixture(() -> new Button("foo"));

        // then:
        assertThatThrownBy(() -> assertThat(button).doesNotHaveText("foo"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Labeled has text \"foo\" to be false\n     " +
                        "but: was \"foo\"");
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
        assertThat(textField).isFocused();
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
        assertThatThrownBy(() -> assertThat(textField).isFocused())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Node has focus\n");
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
        assertThat(textField).isNotFocused();
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
        assertThatThrownBy(() -> assertThat(textField).isNotFocused())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Node does not have focus\n");
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
        assertThat(query1.queryAll()).containsExactly(nodes.get(1));

        NodeQuery query2 = from(nodes).match(TextInputControlMatchers.hasText("bar"));
        assertThat(query2.queryAll()).containsExactly(nodes.get(2));
    }

    @Test
    public void hasChild() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent).hasChild(".button");
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane());

        // then:
        assertThatThrownBy(() -> assertThat(parent).hasChild(".button"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Node has child \".button\"\n");
    }

    @Test
    public void doesNotHaveChild() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent).doesNotHaveChild(".boot");
    }

    @Test
    public void doesNotHaveChild_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThatThrownBy(() -> assertThat(parent).doesNotHaveChild(".button"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Node has child \".button\" to be false\n");
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent).hasExactlyChildren(2, ".button");
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(new Label("foo"), new Button("bar")));

        // then:
        assertThatThrownBy(() -> assertThat(parent).hasExactlyChildren(2, ".button"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Node has 2 children \".button\"\n");
    }
}
