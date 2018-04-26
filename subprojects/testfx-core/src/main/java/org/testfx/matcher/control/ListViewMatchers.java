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

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link ListView} controls.
 */
public class ListViewMatchers {

    private static final String SELECTOR_LIST_CELL = ".list-cell";

    private ListViewMatchers() {}

    /**
     * Creates a matcher that matches all {@link ListView}s that have one cell that equals the given {@code value}.
     *
     * @param value the list cell the matched ListView's should have
     */
    @Factory
    public static Matcher<ListView> hasListCell(Object value) {
        String descriptionText = "has list cell \"" + value + "\"";
        return typeSafeMatcher(ListView.class, descriptionText, ListViewMatchers::getItemsString,
            listView -> hasListCell(listView, value));
    }

    /**
     * Creates a matcher that matches all {@link ListView}s that have exactly {@code amount} items (i.e.
     * sizeof({@link ListView#getItems()}) = {@code amount}).
     *
     * @param amount the number of items the matched ListView's should have
     */
    @Factory
    public static Matcher<ListView> hasItems(int amount) {
        String descriptionText = "has exactly " + amount + ' ' + (amount == 1 ? "item" : "items");
        return typeSafeMatcher(ListView.class, descriptionText,
            listView -> String.valueOf(listView.getItems().size()),
            listView -> listView.getItems().size() == amount);
    }

    /**
     * Creates a matcher that matches all {@link ListView}s that have no items (i.e.
     * sizeof({@link ListView#getItems()}) = 0).
     */
    @Factory
    public static Matcher<ListView> isEmpty() {
        String descriptionText = "is empty (contains no items)";
        return typeSafeMatcher(ListView.class, descriptionText,
            listView -> listView.getItems().size() == 0 ? "empty" : "contains " + listView.getItems().size() + " items",
            listView -> listView.getItems().isEmpty());
    }

    /**
     * Creates a matcher that matches {@link ListView}s that have the given {@code placeHolder}.
     * As a special case if the {@code placeHolder} is an instance of {@link Labeled} then the placeholder
     * matches if the given {@code placeHolder}'s text is equal to the ListView's text.
     *
     * @param placeHolder the placeHolder {@code Node} the matched ListView's should have
     */
    @Factory
    public static Matcher<ListView> hasPlaceholder(Node placeHolder) {
        String descriptionText = "has " + getPlaceHolderDescription(placeHolder, false);
        return typeSafeMatcher(ListView.class, descriptionText,
            listView -> getPlaceHolderDescription(listView.getPlaceholder(), false),
            listView -> hasPlaceholder(listView, placeHolder));
    }

    /**
     * Creates a matcher that matches {@link ListView}s that have the given <em>visible</em> {@code placeHolder}.
     * As a special case if the {@code placeHolder} is an instance of {@link Labeled} then the placeholder
     * matches if the given {@code placeHolder}'s text is equal to the ListView's text and the ListView's
     * placeHolder is visible.
     *
     * @param placeHolder the visible placeHolder {@code Node} the matched ListView's should have
     */
    @Factory
    public static Matcher<ListView> hasVisiblePlaceholder(Node placeHolder) {
        String descriptionText = "has " + getPlaceHolderDescription(placeHolder, true);
        return typeSafeMatcher(ListView.class, descriptionText,
            listView -> getPlaceHolderDescription(listView.getPlaceholder(), true),
            node -> hasVisiblePlaceholder(node, placeHolder));
    }

    private static boolean hasListCell(ListView listView, Object value) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.from(listView);
        return nodeQuery.lookup(SELECTOR_LIST_CELL)
            .<Cell>match(cell -> hasCellValue(cell, value))
            .tryQuery().isPresent();
    }

    private static boolean hasCellValue(Cell cell, Object value) {
        return !cell.isEmpty() && Objects.equals(cell.getItem(), value);
    }

    private static boolean hasPlaceholder(ListView listView, Node placeHolder) {
        if (Labeled.class.isAssignableFrom(placeHolder.getClass()) &&
                Labeled.class.isAssignableFrom(listView.getPlaceholder().getClass())) {
            return ((Labeled) listView.getPlaceholder()).getText()
                    .equals(((Labeled) placeHolder).getText());
        } else {
            return Objects.equals(listView.getPlaceholder(), placeHolder);
        }
    }

    private static boolean hasVisiblePlaceholder(ListView listView, Node placeHolder) {
        return listView.getPlaceholder().isVisible() && hasPlaceholder(listView, placeHolder);
    }

    private static String getItemsString(ListView<?> listView) {
        StringBuilder items = new StringBuilder("[");
        for (int i = 0; i < listView.getItems().size(); i++) {
            items.append(listView.getItems().get(i).toString());
            if (i < listView.getItems().size() - 1) {
                items.append(", ");
            }
        }
        items.append("]");
        return items.toString();
    }

    private static String getPlaceHolderDescription(Node placeHolder, boolean describeVisibility) {
        if (Labeled.class.isAssignableFrom(placeHolder.getClass())) {
            return (describeVisibility ? (placeHolder.isVisible() ? "visible " : "invisible ") : "") +
                    "labeled placeholder containing text: \"" + ((Labeled) placeHolder).getText() + "\"";
        } else {
            return (describeVisibility ? (placeHolder.isVisible() ? "visible " : "invisible ") : "") +
                    "placeholder " + placeHolder;
        }
    }
}
