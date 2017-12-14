/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Cell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link TableView} controls.
 */
@Unstable(reason = "needs more tests")
public class TableViewMatchers {

    private static final String SELECTOR_TABLE_CELL = ".table-cell";

    private TableViewMatchers() {}

    /**
     * Creates a matcher that matches all {@link TableView}s that has a {@link javafx.scene.control.TableCell}
     * whose value or {@code value.toString()} equals the given value.
     */
    @Factory
    public static Matcher<TableView> hasTableCell(Object value) {
        String descriptionText = "has table cell \"" + value + "\"";
        return typeSafeMatcher(TableView.class, descriptionText,
            tableView -> toText(tableView) + "\nwhich does not contain a cell with the given value",
            tableView -> hasTableCell(tableView, value));
    }

    /**
     * Creates a matcher that matches all {@link TableView}s that have exactly {@code amount} items.
     *
     * @deprecated Use {@link #hasNumRows(int amount)} instead.
     */
    @Factory
    @Deprecated
    public static Matcher<TableView> hasItems(int rows) {
        String descriptionText = "has " + rows + " rows";
        return typeSafeMatcher(TableView.class, descriptionText,
            tableView -> String.valueOf(tableView.getItems().size()),
            tableView -> tableView.getItems().size() == rows);
    }

    /**
     * Creates a matcher that matches all {@link TableView}s that have exactly {@code amount} rows.
     */
    @Factory
    public static Matcher<TableView> hasNumRows(int rows) {
        String descriptionText = "has " + rows + " rows";
        return typeSafeMatcher(TableView.class, descriptionText,
            tableView -> String.valueOf(tableView.getItems().size()),
            tableView -> tableView.getItems().size() == rows);
    }

    /**
     * Creates a matcher that matches all {@link TableView}s that have a row at the given {@code index} with
     * all of the given cells.
     */
    @Factory
    public static Matcher<TableView> containsRowAtIndex(int rowIndex, Object... cells) {
        String descriptionText = String.format("has row: %s at index %d", Arrays.toString(cells), rowIndex);
        return typeSafeMatcher(TableView.class, descriptionText,
            tableView -> {
                if (rowIndex < 0) {
                    return "given negative row index";
                } else if (rowIndex >= tableView.getItems().size()) {
                    return "given out-of-bounds row index: " + rowIndex +
                            " (table only has " + tableView.getItems().size() + " rows)";
                } else {
                    return toText(tableView, rowIndex);
                }
            },
            tableView -> containsRowAtIndex(tableView, rowIndex, cells));
    }

    /**
     * Creates a matcher that matches all {@link TableView}s that have a row containing all the given cells.
     */
    @Factory
    public static Matcher<TableView> containsRow(Object...cells) {
        String descriptionText = "has row: " + Arrays.toString(cells);
        return typeSafeMatcher(TableView.class, descriptionText, TableViewMatchers::toText,
            tableView -> containsRow(tableView, cells));
    }

    private static boolean hasTableCell(TableView tableView, Object value) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.from(tableView);
        return nodeQuery.lookup(SELECTOR_TABLE_CELL)
            .<Cell>match(cell -> hasCellValue(cell, value))
            .tryQuery().isPresent();
    }

    private static <T> boolean containsRowAtIndex(TableView<T> tableView, int rowIndex, Object...cells) {
        if (rowIndex < 0 || rowIndex >= tableView.getItems().size()) {
            return false;
        }

        List<ObservableValue<?>> rowValues = getRowValues(tableView, rowIndex);
        for (int i = 0; i < cells.length; i++) {
            if (rowValues.get(i).getValue() == null) {
                if (cells[i] != null) {
                    return false;
                }
            } else {
                if (!rowValues.get(i).getValue().equals(cells[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<ObservableValue<?>> getRowValues(TableView<?> tableView, int rowIndex) {
        Object rowObject = tableView.getItems().get(rowIndex);
        List<ObservableValue<?>> rowValues = new ArrayList<>(tableView.getColumns().size());
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            TableColumn<?, ?> column = tableView.getColumns().get(i);
            TableColumn.CellDataFeatures cellDataFeatures = new TableColumn.CellDataFeatures(
                    tableView, column, rowObject);
            rowValues.add(i, column.getCellValueFactory().call(cellDataFeatures));
        }
        return rowValues;
    }

    /**
     * Returns a textual representation of all rows of the given {@code tableView}.
     */
    private static String toText(TableView<?> tableView) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (int rowIndex = 0; rowIndex < tableView.getItems().size(); rowIndex++) {
            joiner.add(toText(tableView, rowIndex));
        }
        return joiner.toString();
    }

    /**
     * Returns a textual representation of one row (specified by {@code rowIndex}) of the given
     * {@code tableView}.
     */
    private static String toText(TableView<?> tableView, int rowIndex) {
        return '[' + getRowValues(tableView, rowIndex).stream()
                .map(observableValue -> observableValue.getValue() == null ?
                        "null" : observableValue.getValue().toString())
                .collect(Collectors.joining(", ")) + ']';
    }

    private static <T> boolean containsRow(TableView<T> tableView, Object...cells) {
        if (tableView.getItems().isEmpty()) {
            return false;
        }

        Map<Integer, List<ObservableValue<?>>> rowValuesMap = new HashMap<>(tableView.getColumns().size());
        for (int j = 0; j < tableView.getItems().size(); j++) {
            List<ObservableValue<?>> rowValues = getRowValues(tableView, j);
            rowValuesMap.put(j, rowValues);
        }

        for (List<ObservableValue<?>> rowValues : rowValuesMap.values()) {
            for (int i = 0; i < cells.length; i++) {
                if (rowValues.get(i).getValue() == null && cells[i] != null) {
                    break;
                } else if (cells[i] == null && rowValues.get(i).getValue() != null) {
                    break;
                } else if (rowValues.get(i).getValue() != null && !rowValues.get(i).getValue().equals(cells[i])) {
                    break;
                } else {
                    if (i == cells.length - 1) {
                        if (rowValues.get(i).getValue() == null && cells[i] != null) {
                            break;
                        } else if (cells[i] == null && rowValues.get(i).getValue() != null) {
                            break;
                        } else {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean hasCellValue(Cell cell, Object value) {
        return !cell.isEmpty() && hasItemValue(cell.getText(), value);
    }

    private static boolean hasItemValue(Object item, Object value) {
        if (item == null && value == null) {
            return true;
        } else if (item == null || value == null) {
            return false;
        }
        return Objects.equals(item, value) || Objects.equals(item.toString(), value) ||
                (value.toString() != null && Objects.equals(item.toString(), value.toString()));
    }

}
