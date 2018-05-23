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

import java.util.Objects;
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
import org.junit.rules.TestRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneralMatchersTest {

    @Rule
    public TestRule rule = new TestFXRule();

    Node nullNode;
    Pane notMatchingNode;
    Button notParentNode;
    Predicate<Node> notNullNodePredicate = Objects::nonNull;
    Predicate<Parent> hasChildrenParentPredicate = parent -> parent.getChildrenUnmodifiable().size() > 0;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        notMatchingNode = FxToolkit.setupFixture(() -> new Pane());
        notParentNode = FxToolkit.setupFixture(() -> new Button());
    }

    @Test
    public void baseMatcher_with_nullNode() {
        // given:
        Matcher<Node> notNullNodeMatcher = GeneralMatchers.baseMatcher(
            "Node is not null", notNullNodePredicate
        );

        // then:
        assertThatThrownBy(() -> assertThat(nullNode, notNullNodeMatcher))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Node is not null\n" +
                        "     but: was null");
    }

    @Test
    public void typeSafeMatcher_with_notMatchingNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // then:
        assertThatThrownBy(() -> assertThat(notMatchingNode, hasChildrenParentMatcher))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has children\n" +
                        "     but: was <" + notMatchingNode.toString() + ">");
    }

    @Test
    public void typeSafeMatcher_with_nullNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        assertThatThrownBy(() -> assertThat(nullNode, hasChildrenParentMatcher))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has children\n" +
                        "     but: was null");
    }

    @Test
    public void typeSafeMatcher_with_notParentNode() {
        // given:
        Matcher<Node> hasChildrenParentMatcher = GeneralMatchers.typeSafeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // then:
        // TODO: Hint expected type on AssertError explicitly.
        assertThatThrownBy(() -> assertThat(notParentNode, hasChildrenParentMatcher))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has children\n" +
                        "     but: was <" + notParentNode.toString() + ">");
    }

}
