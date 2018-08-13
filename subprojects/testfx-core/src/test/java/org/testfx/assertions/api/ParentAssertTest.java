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

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class ParentAssertTest extends InternalTestCaseBase {

    StackPane parent;
    
    
    @Override
    public Node createComponent() {
        parent = new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz"));
        return parent;
    }

    @Test
    public void hasAnyChild() throws Exception {
        // given (a StackPane with children which is-a Parent):

        // then:
        assertThat(parent).hasAnyChild();
    }

    @Test
    public void hasAnyChild_fails() throws Exception {
        // given (a StackPane with no children which is-a Parent):
        interact(() -> parent.getChildren().clear());

        // then:
        assertThatThrownBy(() -> assertThat(parent).hasAnyChild())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Parent has at least one child\n     " +
                        "but: was empty (contained no children)");
    }

    @Test
    public void hasNoChildren() throws Exception {
        // given (a StackPane with no children which is-a Parent):
        interact(() -> parent.getChildren().clear());

        // then:
        assertThat(parent).hasNoChildren();
    }

    @Test
    public void hasNoChildren_fails() throws Exception {
        // given (a StackPane with 1 child which is-a Parent):
        interact(() -> {
            parent.getChildren().clear();
            parent.getChildren().add(new Button("lol"));
        });

        // then:
        assertThatThrownBy(() -> assertThat(parent).hasNoChildren())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Parent has at least one child to be false\n     " +
                        "but: was [Button] (which has 1 child)");
    }

    @Test
    public void hasNumChildren() throws Exception {
        // given (a StackPane with 3 children which is-a Parent):

        // then:
        assertThat(parent).hasExactlyNumChildren(3);
    }

    @Test
    public void hasNumChildren_fails() throws Exception {
        // given (a StackPane with 2 children which is-a Parent):
        interact(() -> {
            parent.getChildren().clear();
            parent.getChildren().addAll(new Label("foo"), new Button("bar"));
        });

        // then:
        assertThatThrownBy(() -> assertThat(parent).hasExactlyNumChildren(3))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Parent has exactly 3 children\n     " +
                        "but: was [Label, Button] (which has 2 children)");
    }

    @Test
    public void doesNotHaveNumChildren() throws Exception {
        // given (a StackPane with 2 children which is-a Parent):

        // then:
        assertThat(parent).doesNotHaveExactlyNumChildren(4);
    }

    @Test
    public void doesNotHaveNumChildren_fails() throws Exception {
        // given (a StackPane with 2 children which is-a Parent):
        interact(() -> {
            parent.getChildren().clear();
            parent.getChildren().addAll(new Label("foo"), new Button("bar"));
        });

        // then:
        assertThatThrownBy(() -> assertThat(parent).doesNotHaveExactlyNumChildren(2))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Parent has exactly 2 children to be false\n     " +
                        "but: was [Label, Button] (which has 2 children)");
    }
}
