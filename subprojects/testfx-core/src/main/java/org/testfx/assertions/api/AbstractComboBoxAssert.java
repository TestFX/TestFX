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

import javafx.scene.control.ComboBox;

import org.testfx.matcher.control.ComboBoxMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Assertion methods for {@link javafx.scene.control.ComboBox} type.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(ComboBox)}</code>.
 *
 * <h4>Example</h4>
 *
 * The following code:
 *
 * <pre>{@code
 *   ComboBox<String> fruits = new ComboBox<>();
 *   fruits.getItems().addAll("Apple", "Banana", "Cherry");
 *   assertThat(fruits).containsExactlyItemsInOrder("Apple", "Banana", "Cherry");
 * }</pre>
 *
 * will assert that {@code fruits} contains exactly (only) the {@code String}'s
 * "Apple", "Banana", and "Cherry" in order.
 */
public class AbstractComboBoxAssert<SELF extends AbstractComboBoxAssert<SELF, T>, T>
        extends AbstractNodeAssert<SELF> {

    protected AbstractComboBoxAssert(ComboBox<T> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} has exactly the given {@code amount}
     * of items.
     *
     * @param amount the given amount of items to compare the actual amount of items to
     * @return this assertion object
     */
    public SELF hasExactlyNumItems(int amount) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.hasItems(amount)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} does not have exactly the
     * given {@code amount} of items.
     *
     * @param amount the given amount of items to compare the actual amount of items to
     * @return this assertion object
     */
    public SELF doesNotHaveExactlyNumItems(int amount) {
        assertThat(actual).is(fromInverseMatcher(ComboBoxMatchers.hasItems(amount)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} has the given {@code selection}
     * as its' selected item.
     *
     * @param selection the given selection to compare the actual selected item to
     * @return this assertion object
     */
    public SELF hasSelectedItem(T selection) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.hasSelectedItem(selection)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} does not have the given
     * {@code selection} as its' selected item.
     *
     * @param selection the given selection to compare the actual selected item to
     * @return this assertion object
     */
    public SELF doesNotHaveSelectedItem(T selection) {
        assertThat(actual).is(fromInverseMatcher(ComboBoxMatchers.hasSelectedItem(selection)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} contains at least the given
     * {@code items} in any order.
     *
     * @param items the given items to ensure are at least contained in the {@code ComboBox} in
     * any order
     * @return this assertion object
     */
    public SELF containsItems(T... items) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.containsItems(items)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} contains exactly the given
     * {@code items} in any order.
     *
     * @param items the given items to ensure are the only ones contained in the {@code ComboBox}
     * in any order
     * @return this assertion object
     */
    public SELF containsExactlyItems(T... items) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.containsExactlyItems(items)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} contains at least the given
     * {@code items} in order.
     *
     * @param items the given items to ensure are at least contained in the {@code ComboBox} in order
     * @return this assertion object
     */
    public SELF containsItemsInOrder(T... items) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.containsItemsInOrder(items)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.ComboBox} contains exactly the given
     * {@code items} in order.
     *
     * @param items the given items to ensure are the only ones contained in the {@code ComboBox}
     * in order
     * @return this assertion object
     */
    public SELF containsExactlyItemsInOrder(T... items) {
        assertThat(actual).is(fromMatcher(ComboBoxMatchers.containsExactlyItemsInOrder(items)));
        return myself;
    }
}
