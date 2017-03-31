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

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.TestFXRule;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;

public class ParentMatchersTest {
    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void hasChild() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // expect:
        assertThat(parent, ParentMatchers.hasChild());
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(
                new Label("foo"), new Button("bar"), new Button("baz")));

        // expect:
        assertThat(parent, ParentMatchers.hasChildren(3));
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane());

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has child\n");

        assertThat(parent, ParentMatchers.hasChild());
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        Node parent = FxToolkit.setupFixture(() -> new StackPane(new Label("foo"), new Button("bar")));

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has 3 children\n");

        assertThat(parent, ParentMatchers.hasChildren(3));
    }

}
