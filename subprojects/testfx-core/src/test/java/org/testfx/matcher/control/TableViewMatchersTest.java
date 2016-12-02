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
import static org.hamcrest.Matchers.not;

public class TableViewMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
                    ImmutableMap.of("name", "alice", "age", 30),
                    ImmutableMap.of("name", "bob", "age", 31),
                    ImmutableMap.of("name", "carol"),
                    ImmutableMap.of("name", "dave")
                    ));
            TableColumn<Map, String> tableColumn0 = new TableColumn<>("name");
            tableColumn0.setCellValueFactory(new MapValueFactory<>("name"));
            TableColumn<Map, Integer> tableColumn1 = new TableColumn<>("age");
            tableColumn1.setCellValueFactory(new MapValueFactory<>("age"));
            tableView.getColumns().setAll(tableColumn0, tableColumn1);
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
        assertThat(tableView, TableViewMatchers.hasTableCell("bob"));
    }

    @Test
    public void hasTableCell_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has table cell \"foobar\"\n");

        assertThat(tableView, TableViewMatchers.hasTableCell("foobar"));
    }

    @Test
    public void hasTableCell_with_toString() {
        // expect:
        assertThat(tableView, TableViewMatchers.hasTableCell("30"));

        // and:
        assertThat(tableView, TableViewMatchers.hasTableCell(31));
    }

    @Test
    public void hasTableCell_with_null_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has table cell \"null\"\n");

        assertThat(tableView, TableViewMatchers.hasTableCell(null));
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

    @Test
    public void containsRowAtIndex() {
        tableView.setItems(observableArrayList(
                ImmutableMap.of("name", "alice", "age", 30),
                ImmutableMap.of("name", "bob", "age", 31),
                ImmutableMap.of("name", "carol", "age", 42),
                ImmutableMap.of("name", "dave", "age", 55)
        ));

        // expect:
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(0, "alice", 30));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(1, "bob", 31));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(2, "carol", 42));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(3, "dave", 55));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(0, "ebert", 49)));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(1, "alice", 30)));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(4, "ebert", 49)));
    }

    @Test
    public void containsRowAtIndex_with_empty_cells() {
        tableView.setItems(observableArrayList(
                ImmutableMap.of("name", "alice", "age", 30),
                ImmutableMap.of("name", "bob", "age", 31),
                ImmutableMap.of("name", "carol"),
                ImmutableMap.of("name", "dave")
        ));
        // expect:
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(0, "alice", 30));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(1, "bob", 31));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(2, "carol", null));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(3, "dave", null));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(0, "ebert", null)));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(3, "carol", null)));
    }

    @Test
    public void containsRowAtIndex_no_such_row_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has row: [jerry, 29]\n");

        assertThat(tableView, TableViewMatchers.containsRowAtIndex(0, "jerry", 29));
    }

    @Test
    public void containsRowAtIndex_out_of_bounds_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has row: [tom, 54]\n");

        assertThat(tableView, TableViewMatchers.containsRowAtIndex(4, "tom", 54));
    }

    @Test
    public void containsRowAtIndex_wrong_types_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has row: [63, deedee]\n");

        assertThat(tableView, TableViewMatchers.containsRowAtIndex(1, 63, "deedee"));
    }

    @Test
    public void containsRow() {
        tableView.setItems(observableArrayList(
                ImmutableMap.of("name", "alice", "age", 30),
                ImmutableMap.of("name", "bob", "age", 31),
                ImmutableMap.of("name", "carol", "age", 42),
                ImmutableMap.of("name", "dave", "age", 55)
        ));

        // expect:
        assertThat(tableView, TableViewMatchers.containsRow("alice", 30));
        assertThat(tableView, TableViewMatchers.containsRow("bob", 31));
        assertThat(tableView, TableViewMatchers.containsRow("carol", 42));
        assertThat(tableView, TableViewMatchers.containsRow("dave", 55));
        assertThat(tableView, not(TableViewMatchers.containsRow("ebert", 49)));
    }

    @Test
    public void containsRow_with_empty_cells() {
        tableView.setItems(observableArrayList(
                ImmutableMap.of("name", "alice", "age", 30),
                ImmutableMap.of("name", "bob", "age", 31),
                ImmutableMap.of("name", "carol"),
                ImmutableMap.of("name", "dave")
        ));
        // expect:
        assertThat(tableView, TableViewMatchers.containsRow("alice", 30));
        assertThat(tableView, TableViewMatchers.containsRow("bob", 31));
        assertThat(tableView, TableViewMatchers.containsRow("carol", null));
        assertThat(tableView, TableViewMatchers.containsRow("dave", null));
        assertThat(tableView, not(TableViewMatchers.containsRow("ebert", null)));
    }

    @Test
    public void containsRow_no_such_row_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has row: [jerry, 29]\n");

        assertThat(tableView, TableViewMatchers.containsRow("jerry", 29));
    }

    @Test
    public void containsRow_wrong_types_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TableView has row: [63, deedee]\n");

        assertThat(tableView, TableViewMatchers.containsRow(63, "deedee"));
    }
}
