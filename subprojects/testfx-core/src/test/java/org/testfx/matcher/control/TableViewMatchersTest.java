/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.matcher.control;

import java.util.Map;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.StackPane;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableViewMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none().handleAssertionErrors();

    public TableView<Map> tableView;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            tableView = new TableView<>();
            tableView.setItems(observableArrayList(
                ImmutableMap.of("name", "alice"),
                ImmutableMap.of("name", "bob"),
                ImmutableMap.of("name", "carol"),
                ImmutableMap.of("name", "dave")
            ));
            TableColumn<Map, String> tableColumn0 = new TableColumn<>("name");
            tableColumn0.setCellValueFactory(new MapValueFactory<>("name"));
            tableView.getColumns().add(tableColumn0);
            return new StackPane(tableView);
        });
        FxToolkit.showStage();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void hasTableCell() {
        // expect:
        assertThat(tableView, TableViewMatchers.hasTableCell("alice"));
    }

    @Test
    public void hasTableCell_with_null_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has table cell \"null\"\n");

        assertThat(tableView, TableViewMatchers.hasTableCell(null));
    }

    @Test
    public void hasTableCell_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has table cell \"foobar\"\n");

        assertThat(tableView, TableViewMatchers.hasTableCell("foobar"));
    }

    @Test
    public void hasItems() {
        // expect:
        assertThat(tableView, TableViewMatchers.hasItems(4));
    }

    @Test
    public void hasItems_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has 0 items\n");

        assertThat(tableView, TableViewMatchers.hasItems(0));
    }

}
