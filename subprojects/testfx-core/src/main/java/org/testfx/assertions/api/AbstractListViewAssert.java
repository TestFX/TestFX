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
import javafx.scene.control.ListView;

import org.testfx.matcher.control.ListViewMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.control.ListView} assertions.
 */
public class AbstractListViewAssert<SELF extends AbstractListViewAssert<SELF, T>, T>
        extends AbstractNodeAssert<SELF> {

    protected AbstractListViewAssert(ListView<T> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} contains the given list cell
     * {@code value}.
     *
     * @param value the given list cell value to ensure the {@code ListView} contains
     * @return this assertion object
     */
    public SELF hasListCell(Object value) {
        assertThat(actual).is(fromMatcher(ListViewMatchers.hasListCell(value)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} does not contain the
     * given list cell {@code value}.
     *
     * @param value the given list cell value to ensure the {@code ListView} does not contain
     * @return this assertion object
     */
    public SELF doesNotHaveListCell(Object value) {
        assertThat(actual).is(fromInverseMatcher(ListViewMatchers.hasListCell(value)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} has exactly the given {@code amount}
     * of items.
     *
     * @param amount the given amount of items to compare the actual amount of items to
     * @return this assertion object
     */
    public SELF hasExactlyNumItems(int amount) {
        assertThat(actual).is(fromMatcher(ListViewMatchers.hasItems(amount)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} does not have exactly the
     * given {@code amount} of items.
     *
     * @param amount the given amount of items to compare the actual amount of items to
     * @return this assertion object
     */
    public SELF doesNotHaveExactlyNumItems(int amount) {
        assertThat(actual).is(fromInverseMatcher(ListViewMatchers.hasItems(amount)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} is empty (does not contain
     * any items).
     *
     * @return this assertion object
     */
    public SELF isEmpty() {
        assertThat(actual).is(fromMatcher(ListViewMatchers.isEmpty()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} is not empty (contains at
     * least one item).
     *
     * @return this assertion object
     */
    public SELF isNotEmpty() {
        assertThat(actual).is(fromInverseMatcher(ListViewMatchers.isEmpty()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} has the given {@code placeHolder}
     * node.
     *
     * @param placeHolder the given place holder {@code Node} to compare the actual place holder to
     * @return this assertion object
     */
    public SELF hasPlaceholder(Node placeHolder) {
        assertThat(actual).is(fromMatcher(ListViewMatchers.hasPlaceholder(placeHolder)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} does not have the given
     * {@code placeHolder} node.
     *
     * @param placeHolder the given place holder {@code Node} to compare the actual place holder to
     * @return this assertion object
     */
    public SELF doesNotHavePlaceholder(Node placeHolder) {
        assertThat(actual).is(fromInverseMatcher(ListViewMatchers.hasPlaceholder(placeHolder)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} has the given {@code placeHolder}
     * node and that the node is visible.
     *
     * @param placeHolder the given place holder {@code Node} to compare the actual place holder to
     * @return this assertion object
     */
    public SELF hasVisiblePlaceholder(Node placeHolder) {
        assertThat(actual).is(fromMatcher(ListViewMatchers.hasVisiblePlaceholder(placeHolder)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ListView} does not have the given visible
     * {@code placeHolder}. This assertion will pass for any invisible place holder.
     *
     * @param placeHolder the given place holder {@code Node} to compare the actual place holder to
     * @return this assertion object
     */
    public SELF doesNotHaveVisiblePlaceholder(Node placeHolder) {
        assertThat(actual).is(fromInverseMatcher(ListViewMatchers.hasVisiblePlaceholder(placeHolder)));
        return myself;
    }
}
