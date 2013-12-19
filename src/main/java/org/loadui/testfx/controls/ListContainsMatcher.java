package org.loadui.testfx.controls;

import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.loadui.testfx.controls.ListViews.containsRow;
import static org.loadui.testfx.controls.ListViews.getListView;

public class ListContainsMatcher extends BaseMatcher {
    private Object valueToMatch;

    ListContainsMatcher(Object valueToMatch)
    {
        this.valueToMatch = valueToMatch;
    }

    @Override
    public boolean matches(Object o) {
        if( o instanceof String)
        {
            String query = (String) o;
            return containsRow(getListView(query), valueToMatch);
        } else if( o instanceof ListView)
        {
            ListView tableView = (ListView) o;
            return containsRow(tableView, valueToMatch);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("The list does not contain a row with value '" + valueToMatch + "'");
    }
}
