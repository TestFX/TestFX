package org.loadui.testfx.controls;

import javafx.scene.control.TableView;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static org.loadui.testfx.controls.TableViews.containsCell;
import static org.loadui.testfx.controls.TableViews.getTableView;

class TableContainsMatcher extends BaseMatcher
{
    private Object valueToMatch;

    TableContainsMatcher(Object valueToMatch)
    {
        this.valueToMatch = valueToMatch;
    }

    @Override
    public boolean matches(Object o) {
        if( o instanceof String)
        {
            String query = (String) o;
            return containsCell( getTableView(query), valueToMatch );
        } else if( o instanceof TableView )
        {
            TableView tableView = (TableView) o;
            return containsCell( tableView, valueToMatch );
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("The table does not contain a cell with value '" + valueToMatch + "'");
    }
}
