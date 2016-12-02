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
package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBoxBuilder;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.ListViews.containsRow;
import static org.loadui.testfx.controls.ListViews.hasSelectedRow;
import static org.loadui.testfx.controls.ListViews.numberOfRowsIn;

@Category(TestFX.class)
public class ListViewsTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        ListView<Integer> list = new ListView<>();
        list.setItems(observableArrayList(1, 2, 3));
        list.getSelectionModel().select(new Integer(2));
        return VBoxBuilder
            .create()
            .children(list).build();
    }

    @Test
    public void shouldFindRowValues() {
        verifyThat(".list-view", containsRow("1"));
        verifyThat(".list-view", not(containsRow("4")));

        ListView<?> listView = find(".list-view");
        verifyThat(listView, containsRow(1));
    }

    @Test
    public void shouldHaveCorrectNumberOfRows() {
        verifyThat(numberOfRowsIn(".list-view"), is(3));

        ListView<?> listView = find(".list-view");
        verifyThat(numberOfRowsIn(listView), is(3));
    }

    @Test
    public void shouldHaveSelectedTheRow() {
        ListView<?> listView = find(".list-view");

        verifyThat(listView, hasSelectedRow(2));
        verifyThat(listView, not(hasSelectedRow(1)));
        verifyThat(listView, not(hasSelectedRow(3)));
    }

    @Test
    public void shouldHaveSelectedTheRowByString() {
        verifyThat(".list-view", hasSelectedRow("2"));
        verifyThat(".list-view", not(hasSelectedRow("1")));
        verifyThat(".list-view", not(hasSelectedRow("3")));
    }

}
