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

import java.util.function.Predicate;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;

public class GeneralMatchersTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public Node nullNode;

    public Pane notMatchingNode;

    public Button notParentNode;

    public Predicate<Node> notNullNodePredicate =
        node -> node != null;

    public Predicate<Parent> hasChildrenParentPredicate =
        parent -> parent.getChildrenUnmodifiable().size() > 0;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        notMatchingNode = FxToolkit.setupFixture(() -> new Pane());
        notParentNode = FxToolkit.setupFixture(() -> new Button());
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void baseMatcher_with_nullNode() {
        // given:
        Matcher<Node> notNullNodeMatcher = GeneralMatchers.baseMatcher(
            "Node is not null", notNullNodePredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Node is not null\n" +
                                "     but: was null");
        assertThat(nullNode, notNullNodeMatcher);
    }

    @Test
    public void typeSafeMatcher_with_notMatchingNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was <" + notMatchingNode.toString() + ">");
        assertThat(notMatchingNode, hasChildrenParentMatcher);
    }

    @Test
    public void typeSafeMatcher_with_nullNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was null");
        assertThat(nullNode, hasChildrenParentMatcher);
    }

    // java.lang.ClassCastException: javafx.scene.control.Button cannot be cast to javafx.scene.control.TreeView

    @Test
    public void typeSafeMatcher_with_notParentNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        // TODO: Hint expected type on AssertError explicitly.
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was <" + notParentNode.toString() + ">");
        assertThat(notParentNode, hasChildrenParentMatcher);
    }

}
