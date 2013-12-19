package org.loadui.testfx.controls;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
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
    public static int numberOfRowsIn(ListView<?> list)
    {
        return list.getItems().size();
    }

    /**
     * Get the number of rows in the list that matches the CSS query.
     *
     * @param listQuery
     * @return
     */
    public static int numberOfRowsIn(String listQuery)
    {
        ListView<?> table = find(listQuery);
        return table.getItems().size();
    }

    @Factory
    public static org.hamcrest.Matcher containsRow(Object rowValue)
    {
        return new ListContainsMatcher(rowValue);
    }

    static boolean containsRow(ListView<?> list, Object rowValue)
    {
        for(int i=0; i<list.getItems().size(); i++ )
        {
            Object rowData = list.getItems().get(i);
            if( rowValue.equals( rowData ) || rowValue.equals(rowData.toString()) )
                return true;
        }
        return false;
    }

    static ListView<?> getListView(String listSelector) {
        Node node = find(listSelector);
        if (!(node instanceof ListView)) {
            throw new NoNodesFoundException(listSelector + " selected " + node + " which is not a ListView!");
        }
        return (ListView<?>) node;
    }
}
