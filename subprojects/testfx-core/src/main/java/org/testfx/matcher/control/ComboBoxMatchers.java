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
package org.testfx.matcher.control;

import java.util.Arrays;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableView;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

@Unstable(reason = "needs more tests")
public class ComboBoxMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasItems(int amount) {
        String descriptionText = "has " + amount + " items";
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> hasItems(node, amount));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static <T> Matcher<Node> hasSelection(T selection) {
        String descriptionText = "has selection " + selection;
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> hasSelection(node, selection));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static <T> Matcher<Node> containsItems(T... items) {
        String descriptionText = "contains items " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> containsItems(node, items));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static <T> Matcher<Node> containsExactlyItems(T... items) {
        String descriptionText = "contains exactly items " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> containsExactlyItems(node, items));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static <T> Matcher<Node> containsItemsInOrder(T... items) {
        String descriptionText = "contains items in order " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> containsItemsInOrder(node, items));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static <T> Matcher<Node> containsExactlyItemsInOrder(T... items) {
        String descriptionText = "contains exactly items in order " + Arrays.toString(items);
        return typeSafeMatcher(ComboBox.class, descriptionText, node -> containsExactlyItemsInOrder(node, items));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasItems(ComboBox<?> comboBox,
                                    int amount) {
        return comboBox.getItems().size() == amount;
    }

    private static <T> boolean hasSelection(ComboBox<?> comboBox,
                                            T selection) {
        return selection.equals(comboBox.getSelectionModel().getSelectedItem());
    }

    private static boolean containsItems(ComboBox<?> comboBox,
                                         Object... items) {
        return comboBox.getItems().containsAll(Arrays.asList(items));
    }

    private static boolean containsExactlyItems(ComboBox<?> comboBox,
                                         Object... items) {
        return comboBox.getItems().size() == items.length &&
                comboBox.getItems().containsAll(Arrays.asList(items));
    }

    private static boolean containsItemsInOrder(ComboBox<?> comboBox,
                                                Object... items) {
        int index = 0;

        // find start of matching sub-sequence
        while (!comboBox.getItems().get(index).equals(items[0]))
        {
            index++;
        }

        // make sure sub-sequence matches
        for (Object item : items) {
            if (!comboBox.getItems().get(index).equals(item)) {
                return false;
            }
            index++;
        }
        return true;
    }

    private static boolean containsExactlyItemsInOrder(ComboBox<?> comboBox,
                                                       Object... items) {
        int index = 0;
        for (Object item : items) {
            if (!comboBox.getItems().get(index).equals(item)) {
                return false;
            }
            index++;
        }
        return true;
    }
}
