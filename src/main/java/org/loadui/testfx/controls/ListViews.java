package org.loadui.testfx.controls;

import javafx.scene.control.ListView;

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

    public static boolean containsRow(ListView<?> list, Object rowValue)
    {
        for(int i=0; i<list.getItems().size(); i++ )
        {
            Object rowData = list.getItems().get(i);
            if( rowValue.equals( rowData ) )
                return true;
        }
        return false;
    }
}
