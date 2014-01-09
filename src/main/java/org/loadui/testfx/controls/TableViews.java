package org.loadui.testfx.controls;

import com.google.common.base.Predicate;

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

import java.util.List;

import static org.loadui.testfx.GuiTest.find;

/**
 * Utility methods for TableView.
 *
 * "Clicking a row/cell & asserting its value would be great. My feature wish list: double/right-click row, select 1+ rows, edit a cell"
 *
 * @author Philipp91
 * @author minisu
 */
public class TableViews
{
    /**
     * Get the number of rows in the table.
     *
     * @param table
     * @return number of rows in table
     */
    public static int numberOfRowsIn(TableView<?> table)
    {
        return table.getItems().size();
    }

    /**
     * Get the number of rows in the table that matches the CSS query tableQuery.
     *
     * @param tableQuery
     * @return
     */
    public static int numberOfRowsIn(String tableQuery)
    {
        TableView<?> table = find(tableQuery);
        return table.getItems().size();
    }

    @SuppressWarnings("unchecked")
    @Factory
    public static <S> Matcher<S> containsCell(Object cellValue)
    {
        return new TableContainsMatcher(cellValue);
    }

    static boolean containsCell(TableView<?> table, Predicate<String> cellPredicate)
    {
        for( TableColumn<?, ?> column : table.getColumns() )
        {
            for(int i=0; i<table.getItems().size(); i++ )
            {
                Object cellData = column.getCellData( i );
                if( cellPredicate.apply( cellData.toString() ) )
                    return true;
            }
        }
        return false;
    }

    static boolean containsCell(TableView<?> table, Object cellValue)
    {
        for( TableColumn<?, ?> column : table.getColumns() )
        {
            for(int i=0; i<table.getItems().size(); i++ )
            {
                Object cellData = column.getCellData( i );
                if( cellValue.equals( cellData ) || cellValue.equals( cellData.toString() ) )
                    return true;
            }
        }
        return false;
    }

    /**
     * @param tableSelector Ein CSS-Selektor oder Label.
     * @return Die TableView, sofern sie anhand des Selektors gefunden wurde.
     */
    static TableView<?> getTableView(String tableSelector) {
        Node node = find(tableSelector);
        if (!(node instanceof TableView)) {
            throw new NoNodesFoundException(tableSelector + " selected " + node + " which is not a TableView!");
        }
        return (TableView<?>) node;
    }

    /**
     * @param tableSelector CSS selector
     * @param row row number
     * @param column column number
     * @return Der Wert der gegebenen Zelle in der Tabelle. Es handelt sich nicht um das, was auf der UI dransteht,
     *         sondern um den Wert, also nicht notwendigerweise ein String.
     */
    protected static Object cellValue(String tableSelector, int row, int column) {
        return getTableView(tableSelector).getColumns().get(column).getCellData(row);
    }

    /**
     * @param tableSelector Selektor zur Identifikation der Tabelle.
     * @param row Zeilennummer
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
        } else {
            throw new RuntimeException("Expected Group with only TableRows as children");
        }
    }

    /**
     * @param tableSelector Selektor zur Identifikation der Tabelle.
     * @param row Zeilennummer
     * @param column Spaltennummer
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
        } else {
            throw new RuntimeException("Expected TableRowSkin with only TableCells as children");
        }
    }

    @SuppressWarnings("rawtypes")
    private static class TableContainsMatcher extends BaseMatcher
    {
        private Object valueToMatch;

        public TableContainsMatcher(Object valueToMatch)
        {
            this.valueToMatch = valueToMatch;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean matches(Object o) {
            if( o instanceof String)
            {
                String query = (String) o;
                if( valueToMatch instanceof Predicate )
                    return TableViews.containsCell(getTableView(query), (Predicate) valueToMatch);
                return TableViews.containsCell(getTableView(query), valueToMatch);
            } else if( o instanceof TableView )
            {
                TableView tableView = (TableView) o;
                if( valueToMatch instanceof Predicate )
                    return TableViews.containsCell(tableView, (Predicate) valueToMatch);
                return TableViews.containsCell(tableView, valueToMatch);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("The table does not contain a cell with value '" + valueToMatch + "'");
        }
    }
}
