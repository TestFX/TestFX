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
package org.testfx.matcher.control;

import java.util.Arrays;

import javafx.scene.control.ComboBox;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link ComboBox} controls.
 *
 * <h4>Example</h4>
 *
 * The following code:
 *
 * <pre>{@code
 *   ComboBox<String> fruits = new ComboBox<>();
 *   fruits.getItems().addAll("Apple", "Banana", "Cherry");
 *   assertThat(fruits, ComboBoxMatchers.containsExactlyItemsInOrder("Apple", "Banana", "Cherry"));
 * }</pre>
 *
 * will verify that {@code fruits} contains exactly (only) the {@code String}'s
 * "Apple", "Banana", and "Cherry" in order.
 */
public class ComboBoxMatchers {

    private ComboBoxMatchers() {}

    /**
     * Creates a matcher that matches all {@link ComboBox}es that have exactly {@code amount} items.
     *
     * @param amount the number of items the matched ComboBox's should have
     */
    @Factory
    public static Matcher<ComboBox> hasItems(int amount) {
        String descriptionText = "has exactly " + amount + " items";
        return typeSafeMatcher(ComboBox.class, descriptionText,
            comboBox -> String.valueOf(comboBox.getItems().size()),
            comboBox -> comboBox.getItems().size() == amount);
    }

    /**
     * Creates a matcher that matches all {@link ComboBox}es that have given {@code selection} as
     * its selected item.
     *
     * @param selection the selected item the matched ComboBox's should have
     */
    @Factory
    public static <T> Matcher<ComboBox> hasSelectedItem(T selection) {
        String descriptionText = String.format("has selection \"%s\"", selection);
        return typeSafeMatcher(ComboBox.class, descriptionText,
            comboBox -> "\"" + comboBox.getSelectionModel().getSelectedItem().toString() + "\"",
            comboBox -> hasSelectedItem(comboBox, selection));
    }

    /**
     * Creates a matcher that matches all {@link ComboBox}es that have all of the given {@code items}, regardless
     * of whether it also contains other items and regardless of their order of appearance.
     *
     * @param items the items the matched ComboBox's should have
     */
    @Factory
    public static <T> Matcher<ComboBox> containsItems(T... items) {
        String descriptionText = "contains items " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, ComboBoxMatchers::getItemsString,
            comboBox -> containsItems(comboBox, items));
    }

    /**
     * Creates a matcher that matches all {@link ComboBox}es that only have all of the given {@code items},
     * regardless of the order of their appearance.
     *
     * @param items the only items the matched ComboBox's should have
     */
    @Factory
    public static <T> Matcher<ComboBox> containsExactlyItems(T... items) {
        String descriptionText = "contains exactly items " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, ComboBoxMatchers::getItemsString,
            comboBox -> containsExactlyItems(comboBox, items));
    }

    /**
     * Creates a matcher that matches all {@link ComboBox}es that have all of the given {@code items} in
     * the exact order they appear, regardless of whether it also contains other items before or after this
     * exact sequence.
     *
     * @param items the items the matched ComboBox's should have in the same order
     */
    @Factory
    public static <T> Matcher<ComboBox> containsItemsInOrder(T... items) {
        String descriptionText = "contains items in order " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, ComboBoxMatchers::getItemsString,
            comboBox -> containsItemsInOrder(comboBox, items));
    }

    /**
     * Creates a matcher that matches all {@link ComboBox}es that only have all of the given {@code items} in
     * the exact order they are given.
     *
     * @param items the only items the matched ComboBox's should have in the same order
     */
    @Factory
    public static <T> Matcher<ComboBox> containsExactlyItemsInOrder(T... items) {
        String descriptionText = "contains exactly items in order " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, ComboBoxMatchers::getItemsString,
            comboBox -> containsExactlyItemsInOrder(comboBox, items));
    }

    private static <T> boolean hasSelectedItem(ComboBox<?> comboBox, T selection) {
        return selection.equals(comboBox.getSelectionModel().getSelectedItem());
    }

    private static boolean containsItems(ComboBox<?> comboBox, Object... items) {
        return comboBox.getItems().containsAll(Arrays.asList(items));
    }

    private static boolean containsExactlyItems(ComboBox<?> comboBox, Object... items) {
        return comboBox.getItems().size() == items.length &&
                comboBox.getItems().containsAll(Arrays.asList(items));
    }

    private static boolean containsItemsInOrder(ComboBox<?> comboBox, Object... items) {
        int index = 0;

        // find start of matching sub-sequence
        while (!comboBox.getItems().get(index).equals(items[0])) {
            if (items.length >= comboBox.getItems().size() - index) {
                return false;
            }
            index++;
        }

        // make sure sub-sequence matches
        return matchSubSequenceInOrder(comboBox, index, items);
    }

    private static boolean containsExactlyItemsInOrder(ComboBox<?> comboBox, Object... items) {
        return matchSubSequenceInOrder(comboBox, 0, items);
    }

    /**
     * If startIndex = 0, this method effectively matches the entire sequence.
     */
    private static boolean matchSubSequenceInOrder(ComboBox<?> comboBox, int startIndex, Object... items) {
        int index = startIndex;
        for (Object item : items) {
            if (index >= comboBox.getItems().size() ||
                    !comboBox.getItems().get(index).equals(item)) {
                return false;
            }
            index++;
        }
        return true;
    }

    private static String getItemsString(ComboBox<?> comboBox) {
        StringBuilder items = new StringBuilder("[");
        for (int i = 0; i < comboBox.getItems().size(); i++) {
            items.append(comboBox.getItems().get(i).toString());
            if (i < comboBox.getItems().size() - 1) {
                items.append(", ");
            }
        }
        items.append("]");
        return items.toString();
    }
}
