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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.loadui.testfx.exceptions.NoNodesFoundException;

import static org.loadui.testfx.GuiTest.find;

/**
 * Utility methods for TableView.
 *
 * Clicking a row/cell and asserting its value would be great. My feature wish list:
 * double/right-click row, select 1+ rows, edit a cell.
 *
 */
public class TableViews {

    /**
     * Get the number of rows in the table.
     *
     * @param table
     * @return number of rows in table
     */
    public static int numberOfRowsIn(TableView<?> table) {
        return table.getItems().size();
    }

    /**
     * Get the number of rows in the table that matches the CSS query tableQuery.
     *
     * @param tableQuery
     * @return
     */
    public static int numberOfRowsIn(String tableQuery) {
        TableView<?> table = find(tableQuery);
        return table.getItems().size();
    }

    @SuppressWarnings("unchecked")
    @Factory
    public static <S> Matcher<S> containsCell(Object cellValue) {
        return new TableContainsMatcher(cellValue);
    }

    static List<TableColumn> flatten(TableColumn col) {
        if (col.getColumns().size() == 0) {
            return Arrays.asList(col);
        }
        else {
          List<TableColumn> l = new ArrayList<>();
          col.getColumns()
              .forEach(xa -> l.addAll(flatten((TableColumn) xa)));
            return l;
        }
    }

    static List<TableColumn> getFlattenedColumns(TableView<?> table) {
      List<TableColumn> l = new ArrayList<>();
      table.getColumns()
          .forEach(c -> l.addAll(flatten(c)));
        return l;
    }

    static boolean containsCell(TableView<?> table, Predicate<String> cellPredicate) {
//      return getFlattenedColumns(table)
//          .stream()
//          .map(col -> {col.})
//          .anyMatch(cellData -> cellPredicate.apply(cellData.toString()));

      for (TableColumn<?, ?> column : getFlattenedColumns(table)) {
          for (int i = 0; i < table.getItems().size(); i++) {
              Object cellData = column.getCellData(i);
              if (cellPredicate.test(cellData.toString())) {
                  return true;
              }
          }
      }
      return false;
    }

    static boolean containsCell(TableView<?> table, Object cellValue) {
//     return  getFlattenedColumns(table)
//         .stream()
//         .map(v -> {
//         })
//         .anyMatch(cellData -> cellValue.equals(cellData) || cellValue.equals(cellData.toString()));
      for (TableColumn<?, ?> column : getFlattenedColumns(table)) {
            for (int i = 0; i < table.getItems().size(); i++) {
                Object cellData = column.getCellData(i);
                if (cellValue.equals(cellData) || cellValue.equals(cellData.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param tableSelector Ein CSS-Selektor oder Label.     //TODO translate to english
     * @return Die TableView, sofern sie anhand des Selektors gefunden wurde.
     */
    static TableView<?> getTableView(String tableSelector) {
        Node node = find(tableSelector);
        if (!(node instanceof TableView)) {
            throw new NoNodesFoundException(tableSelector + " selected " + node +
                " which is not a TableView!");
        }
        return (TableView<?>) node;
    }

    /**
     * @param tableSelector CSS selector
     * @param row           row number
     * @param column        column number
     * @return Der Wert der gegebenen Zelle in der Tabelle. Es handelt sich nicht um das, was auf
     * der UI dransteht,
     * sondern um den Wert, also nicht notwendigerweise ein String.
     */
    protected static Object cellValue(String tableSelector, int row, int column) {
        return getTableView(tableSelector).getColumns().get(column).getCellData(row);
    }

    /**
     * @param tableSelector Selektor zur Identifikation der Tabelle. //TODO translate to english
     * @param row           Zeilennummer
     * @return Die entsprechende Zeile.
     */
    protected static TableRow<?> row(String tableSelector, int row) {

        TableView<?> tableView = getTableView(tableSelector);

        List<Node> current = tableView.getChildrenUnmodifiable();
        while (current.size() == 1) {
            current = ((Parent) current.get(0)).getChildrenUnmodifiable();
        }

        current = ((Parent) current.get(1)).getChildrenUnmodifiable();
        while (!(current.get(0) instanceof TableRow)) {
            current = ((Parent) current.get(0)).getChildrenUnmodifiable();
        }

        Node node = current.get(row);
        if (node instanceof TableRow) {
            return (TableRow<?>) node;
        }
        else {
            throw new RuntimeException("Expected Group with only TableRows as children");
        }
    }

    /**
     * @param tableSelector Selektor zur Identifikation der Tabelle. //TODO translate to english
     * @param row           Zeilennummer
     * @param column        Spaltennummer
     * @return Die entsprechende Zelle.
     */
    protected static TableCell<?, ?> cell(String tableSelector, int row, int column) {
        List<Node> current = row(tableSelector, row).getChildrenUnmodifiable();
        while (current.size() == 1 && !(current.get(0) instanceof TableCell)) {
            current = ((Parent) current.get(0)).getChildrenUnmodifiable();
        }

        Node node = current.get(column);
        if (node instanceof TableCell) {
            return (TableCell<?, ?>) node;
        }
        else {
            throw new RuntimeException("Expected TableRowSkin with only TableCells as children");
        }
    }

    @SuppressWarnings("rawtypes")
    private static class TableContainsMatcher extends BaseMatcher {
        private Object valueToMatch;

        public TableContainsMatcher(Object valueToMatch) {
            this.valueToMatch = valueToMatch;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String query = (String) o;
                if (valueToMatch instanceof Predicate) {
                    return TableViews.containsCell(getTableView(query), (Predicate) valueToMatch);
                }
                return TableViews.containsCell(getTableView(query), valueToMatch);
            }
            else if (o instanceof TableView) {
                TableView tableView = (TableView) o;
                if (valueToMatch instanceof Predicate) {
                    return TableViews.containsCell(tableView, (Predicate) valueToMatch);
                }
                return TableViews.containsCell(tableView, valueToMatch);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("The table does not contain a cell with value '" +
                valueToMatch + "'");
        }
    }

}
