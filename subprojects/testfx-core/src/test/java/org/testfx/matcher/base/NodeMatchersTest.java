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
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class NodeMatchersTest extends InternalTestCaseBase {


    VBox parent;
    TextField textField;
    TextField textField2;
    Button btnBar;
    Button btnBaz;

    @Override
    public Node createComponent() {
        textField = new TextField("foo");
        textField2 = new TextField("bar");
        btnBar = new Button("bar");
        btnBaz = new Button("baz");
        parent = new VBox();
        parent.getChildren().addAll(textField, textField2, btnBar, btnBaz);
        return parent;
    }

    @Test
    public void anything() throws Exception {
        assertThat(from(parent.getChildren()).match(NodeMatchers.anything()).queryAll(), hasItem(btnBar));
    }

    @Test
    public void isFocused() throws Exception {
        // given:

        // when:
        Platform.runLater(() -> textField.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();


        // then:
        textField.isFocused();
        assertThat(textField, NodeMatchers.isFocused());
    }

    @Test
    public void isFocused_fails() throws Exception {
        // given:

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

        Platform.runLater(() -> textField2.requestFocus());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(textField, NodeMatchers.isNotFocused());
    }

    @Test
    public void isNotFocused_fails() throws Exception {
        // given:

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

        //shouldn't this work too?
        //NodeQuery query2 = from(parent.getChildren()).match(TextInputControlMatchers.hasText("bar"));
        //assertThat(query2.queryAll(), hasItems(btnBar));
    }

    @Test
    public void hasChild() throws Exception {
        // given:

        // then:
        assertThat(parent, NodeMatchers.hasChild(".button"));
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        interact(() -> parent.getChildren().clear());

        // then:
        assertThatThrownBy(() -> assertThat(parent, NodeMatchers.hasChild(".button")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node has child \".button\"\n");
    }

    @Test
    public void hasChildren() throws Exception {
        // given:

        // then:
        assertThat(parent, NodeMatchers.hasChildren(2, ".button"));
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:

        // then:
        assertThatThrownBy(() -> assertThat(parent, NodeMatchers.hasChildren(3, ".button")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Node has 3 children \".button\"\n");
    }

}
