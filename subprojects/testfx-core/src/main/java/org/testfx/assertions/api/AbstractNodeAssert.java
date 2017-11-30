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

import org.assertj.core.api.AbstractAssert;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.Node} assertions.
 */
public class AbstractNodeAssert<SELF extends AbstractNodeAssert<SELF>> extends AbstractAssert<SELF, Node> {

    protected AbstractNodeAssert(Node actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} is visible.
     *
     * @return this assertion object
     */
    public SELF isVisible() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isVisible()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} is not visible.
     *
     * @return this assertion object
     */
    public SELF isInvisible() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isInvisible()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} is enabled.
     *
     * @return this assertion object
     */
    public SELF isEnabled() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isEnabled()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} is disabled.
     *
     * @return this assertion object
     */
    public SELF isDisabled() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isDisabled()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} has focus.
     *
     * @return this assertion object
     */
    public SELF isFocused() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isFocused()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} does not have focus.
     *
     * @return this assertion object
     */
    public SELF isNotFocused() {
        assertThat(actual).is(fromMatcher(NodeMatchers.isNotFocused()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} has a specific child {@code Node}.
     * The child {@code Node} to look for is specified by the given {@code query}, which
     * is passed to {@link org.testfx.service.query.NodeQuery#lookup(String)}.
     *
     * @param query the node query that specifies the child to look for in the actual {@link Node}
     * @return this assertion object
     */
    public SELF hasChild(String query) {
        assertThat(actual).is(fromMatcher(NodeMatchers.hasChild(query)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} does not have a specific child {@code Node}.
     * The child {@code Node} to look for is specified by the given {@code query}, which
     * is passed to {@link org.testfx.service.query.NodeQuery#lookup(String)}.
     *
     * @param query the node query that specifies the child to look for in the actual {@link Node}
     * @return this assertion object
     */
    public SELF doesNotHaveChild(String query) {
        assertThat(actual).is(fromInverseMatcher(NodeMatchers.hasChild(query)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Node} has exactly the given {@code amount}
     * of children that are looked up by the given {@code query}, which is passed to
     * is passed to {@link org.testfx.service.query.NodeQuery#lookup(String)}.
     *
     * @param amount the given amount of children the actual {@code Node} must exactly have
     * @param query the node query that specifies the children to look for in the actual {@link Node}
     * @return this assertion object
     */
    public SELF hasExactlyChildren(int amount, String query) {
        assertThat(actual).is(fromMatcher(NodeMatchers.hasChildren(amount, query)));
        return myself;
    }
}
