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

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParentMatchersTest {

    @Rule
    public TestRule rule = new TestFXRule();

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void hasChild() throws Exception {
        // given:
        Parent parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent, ParentMatchers.hasChild());
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        Parent parent = FxToolkit.setupFixture(() -> new StackPane());

        // then:
        assertThatThrownBy(() -> assertThat(parent, ParentMatchers.hasChild()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has at least one child\n     " +
                        "but: was empty (contained no children)");
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        Parent parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // then:
        assertThat(parent, ParentMatchers.hasChildren(3));
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        Parent parent = FxToolkit.setupFixture(() -> new StackPane(new Label("foo"), new Button("bar")));

        // then:
        assertThatThrownBy(() -> assertThat(parent, ParentMatchers.hasChildren(3)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has exactly 3 children\n     " +
                        "but: was [Label, Button] (which has 2 children)");
    }

}
