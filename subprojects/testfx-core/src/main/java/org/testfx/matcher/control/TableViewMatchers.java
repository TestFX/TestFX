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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
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

@Unstable(reason = "needs more tests")
public class TableViewMatchers {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String SELECTOR_TABLE_CELL = ".table-cell";

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasTableCell(Object value) {
        String descriptionText = "has table cell \"" + value + "\"";
        return typeSafeMatcher(TableView.class, descriptionText,
            node -> hasTableCell(node, value));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasItems(int amount) {
        String descriptionText = "has " + amount + " items";
        return typeSafeMatcher(TableView.class, descriptionText, node -> hasItems(node, amount));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> containsRowAtIndex(int rowIndex, Object...cells) {
        String descriptionText = "has row: " + Arrays.toString(cells);
        return typeSafeMatcher(TableView.class, descriptionText,
             node -> containsRowAtIndex(node, rowIndex, cells));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> containsRow(Object...cells) {
        String descriptionText = "has row: " + Arrays.toString(cells);
        return typeSafeMatcher(TableView.class, descriptionText,
                node -> containsRow(node, cells));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasTableCell(TableView tableView,
                                        Object value) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.from(tableView);
        return nodeQuery.lookup(SELECTOR_TABLE_CELL)
            .<Cell>match(cell -> hasCellValue(cell, value))
            .tryQuery().isPresent();
    }

    private static boolean hasItems(TableView tableView,
                                    int amount) {
        return tableView.getItems().size() == amount;
    }

    private static <T> boolean containsRowAtIndex(TableView<T> tableView, int rowIndex,
                                                  Object...cells) {
        if (rowIndex >= tableView.getItems().size()) {
            return false;
        }

        T rowObject = tableView.getItems().get(rowIndex);
        List<ObservableValue<?>> rowValues = new ArrayList<>(tableView.getColumns().size());
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            TableColumn<T, ?> column = tableView.getColumns().get(i);
            TableColumn.CellDataFeatures cellDataFeatures = new TableColumn.CellDataFeatures<>(
                    tableView, column, rowObject);
            rowValues.add(i, column.getCellValueFactory().call(cellDataFeatures));
        }
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

    private static <T> boolean containsRow(TableView<T> tableView, Object...cells) {
        if (tableView.getItems().isEmpty()) {
            return false;
        }

        Map<Integer, List<ObservableValue<?>>> rowValuesMap = new HashMap<>(tableView.getColumns().size());
        for (int j = 0; j < tableView.getItems().size(); j++) {
            T rowObject = tableView.getItems().get(j);
            List<ObservableValue<?>> rowValues = new ArrayList<>(tableView.getColumns().size());

            for (int i = 0; i < tableView.getColumns().size(); i++) {
                TableColumn<T, ?> column = tableView.getColumns().get(i);
                TableColumn.CellDataFeatures cellDataFeatures = new TableColumn.CellDataFeatures<>(
                        tableView, column, rowObject);
                rowValues.add(i, column.getCellValueFactory().call(cellDataFeatures));
            }
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

    private static boolean hasCellValue(Cell cell,
                                        Object value) {
        return !cell.isEmpty() && hasItemValue(cell.getItem(), value);
    }

    private static boolean hasItemValue(Object item,
                                        Object value) {
        return Objects.equals(item, value) || Objects.equals(item.toString(), value);
    }

}
