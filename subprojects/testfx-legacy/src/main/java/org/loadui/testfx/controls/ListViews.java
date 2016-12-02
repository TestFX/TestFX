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
package org.loadui.testfx.controls;

import javafx.scene.Node;
import javafx.scene.control.ListView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.loadui.testfx.exceptions.NoNodesFoundException;

import static org.loadui.testfx.GuiTest.find;

public class ListViews {

    /**
     * Get the number of rows in the list.
     *
     * @param list
     * @return number of rows in list
     */
    public static int numberOfRowsIn(ListView<?> list) {
        return list.getItems().size();
    }

    /**
     * Get the number of rows in the list that matches the CSS query.
     *
     * @param listQuery
     * @return
     */
    public static int numberOfRowsIn(String listQuery) {
        ListView<?> table = find(listQuery);
        return table.getItems().size();
    }

    @SuppressWarnings("unchecked")
    @Factory
    public static <S> org.hamcrest.Matcher<S> containsRow(Object rowValue) {
        return new ListContainsMatcher(rowValue);
    }

    static boolean containsRow(ListView<?> list, Object rowValue) {
      return list.getItems()
          .stream()
          .anyMatch(rowData -> rowValue.equals(rowData) || rowValue.equals(rowData.toString()));
    }

    @SuppressWarnings("unchecked")
    @Factory
    public static <S> org.hamcrest.Matcher<S> hasSelectedRow(Object rowValue) {
        return new ListSelectedMatcher(rowValue);
    }

    static boolean hasSelectedRow(ListView<?> list, Object rowValue) {
      return list.getSelectionModel().getSelectedItems()
          .stream()
          .anyMatch(rowData -> rowValue.equals(rowData) || rowValue.equals(rowData.toString()));
    }

    static ListView<?> getListView(String listSelector) {
        Node node = find(listSelector);
        if (!(node instanceof ListView)) {
            throw new NoNodesFoundException(listSelector + " selected " + node +
                " which is not a ListView!");
        }
        return (ListView<?>) node;
    }

    @SuppressWarnings("rawtypes")
    private static class ListContainsMatcher extends BaseMatcher {
        private Object valueToMatch;

        public ListContainsMatcher(Object valueToMatch) {
            this.valueToMatch = valueToMatch;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String query = (String) o;
                return ListViews.containsRow(getListView(query), valueToMatch);
            }
            else if (o instanceof ListView) {
                ListView tableView = (ListView) o;
                return ListViews.containsRow(tableView, valueToMatch);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("The list does not contain a row with value '" +
                valueToMatch + "'");
        }
    }

    @SuppressWarnings("rawtypes")
    private static class ListSelectedMatcher extends BaseMatcher {
        private Object valueToMatch;

        public ListSelectedMatcher(Object valueToMatch) {
            this.valueToMatch = valueToMatch;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String query = (String) o;
                return ListViews.hasSelectedRow(getListView(query), valueToMatch);
            }
            else if (o instanceof ListView) {
                ListView tableView = (ListView) o;
                return ListViews.hasSelectedRow(tableView, valueToMatch);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("The list does not have selected a row with value '" +
                valueToMatch + "'");
        }
    }

}
