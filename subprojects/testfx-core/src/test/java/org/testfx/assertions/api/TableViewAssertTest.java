/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.assertions.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TableViewAssertTest extends FxRobot {

    @Rule
    public TestFXRule testFXRule = new TestFXRule(3);
    TableView<Map> tableView;
    TableColumn<Map, String> tableColumn0;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            tableView = new TableView<>();
            Map<String, Object> row1 = new HashMap<>(2);
            row1.put("name", "alice");
            row1.put("age", 30);

            Map<String, Object> row2 = new HashMap<>(2);
            row2.put("name", "bob");
            row2.put("age", 31);

            Map<String, Object> row3 = new HashMap<>(1);
            row3.put("name", "carol");

            Map<String, Object> row4 = new HashMap<>(1);
            row4.put("name", "dave");

            tableView.setItems(observableArrayList(row1, row2, row3, row4));
            tableColumn0 = new TableColumn<>("name");
            tableColumn0.setCellValueFactory(new MapValueFactory<>("name"));
            TableColumn<Map, Integer> tableColumn1 = new TableColumn<>("age");
            tableColumn1.setCellValueFactory(new MapValueFactory<>("age"));
            tableView.getColumns().setAll(tableColumn0, tableColumn1);
            return new StackPane(tableView);
        });
        FxToolkit.showStage();
    }

    @Test
    public void hasTableCell() {
        assertThat(tableView).hasTableCell("alice");
        assertThat(tableView).hasTableCell("bob");
    }

    @Test
    public void hasTableCell_customCellValueFactory() {
        // given:
        Platform.runLater(() ->
                tableColumn0.setCellFactory(column -> new TableCell<Map, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.toUpperCase(Locale.US).concat("!"));
                        }
                    }
                }));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).hasTableCell("ALICE!");
        assertThat(tableView).hasTableCell("BOB!");
    }

    @Test
    public void hasTableCell_fails() {
        assertThatThrownBy(() -> assertThat(tableView).hasTableCell("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has table cell \"foobar\"\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void hasTableCell_fails_customCellValueFactory() {
        // given:
        Platform.runLater(() ->
                tableColumn0.setCellFactory(column -> new TableCell<Map, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.toUpperCase(Locale.US).concat("!"));
                        }
                    }
                }));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(tableView).hasTableCell("ALICE!!!"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: TableView has table cell \"ALICE!!!\"\n     ");
        // FIXME(mike): Currently the table is printed without applying the cell value factory
        // once that is fixed, add these lines:
        // "but: was [[ALICE!, 30], [BOB!, 31], [CAROL!, null], [DAVE!, null]]");
    }

    @Test
    public void doesNotHaveTableCell() {
        assertThat(tableView).doesNotHaveTableCell("june");
        assertThat(tableView).doesNotHaveTableCell("july");
    }

    @Test
    public void hasTableCell_with_toString() {
        assertThat(tableView).hasTableCell("30");
        assertThat(tableView).hasTableCell(31);
    }

    @Test
    public void hasTableCell_with_null_fails() {
        // FIXME: This works but it is nonsensical - why can't we accept null?
        assertThatThrownBy(() -> assertThat(tableView).hasTableCell(null))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has table cell \"null\"\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void doesNotHaveTableCell_customCellValueFactory() {
        // given:
        Platform.runLater(() ->
                tableColumn0.setCellFactory(column -> new TableCell<Map, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.toUpperCase(Locale.US).concat("!"));
                        }
                    }
                }));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).doesNotHaveTableCell("alice");
        assertThat(tableView).doesNotHaveTableCell("bob");
    }

    @Test
    public void doesNotHaveTableCell_fails() {
        assertThatThrownBy(() -> assertThat(tableView).doesNotHaveTableCell("alice"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has table cell \"alice\" to be false\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void doesNotHaveTableCell_with_toString() {
        assertThat(tableView).doesNotHaveTableCell("44");
        assertThat(tableView).doesNotHaveTableCell(44);
    }

    @Test
    public void hasExactlyNumRows() {
        assertThat(tableView).hasExactlyNumRows(4);
    }

    @Test
    public void hasExactlyNumRows_fails() {
        assertThatThrownBy(() -> assertThat(tableView).hasExactlyNumRows(0))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has 0 rows\n     " +
                        "but: was contained 4 rows");
    }

    @Test
    public void doesNotHaveExactlyNumRows() {
        assertThat(tableView).doesNotHaveExactlyNumRows(5);
    }

    @Test
    public void doesNotHaveExactlyNumRows_fails() {
        assertThatThrownBy(() -> assertThat(tableView).doesNotHaveExactlyNumRows(4))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has 4 rows to be false\n     " +
                        "but: was contained 4 rows");
    }

    @Test
    public void containsRowAtIndex() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(2);
        row3.put("name", "carol");
        row3.put("age", 42);

        Map<String, Object> row4 = new HashMap<>(2);
        row4.put("name", "dave");
        row4.put("age", 55);

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).containsRowAtIndex(0, "alice", 30);
        assertThat(tableView).containsRowAtIndex(1, "bob", 31);
        assertThat(tableView).containsRowAtIndex(2, "carol", 42);
        assertThat(tableView).containsRowAtIndex(3, "dave", 55);
    }

    @Test
    public void containsRowAtIndex_with_empty_cells() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(1);
        row3.put("name", "carol");

        Map<String, Object> row4 = new HashMap<>(1);
        row4.put("name", "dave");

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).containsRowAtIndex(0, "alice", 30);
        assertThat(tableView).containsRowAtIndex(1, "bob", 31);
        assertThat(tableView).containsRowAtIndex(2, "carol", null);
        assertThat(tableView).containsRowAtIndex(3, "dave", null);
    }

    @Test
    public void containsRowAtIndex_no_such_row_fails() {
        assertThatThrownBy(() -> assertThat(tableView).containsRowAtIndex(0, "jerry", 29))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [jerry, 29] at index 0\n     " +
                        "but: was [alice, 30] at index: 0");
    }

    @Test
    public void containsRowAtIndex_out_of_bounds_fails() {
        assertThatThrownBy(() -> assertThat(tableView).containsRowAtIndex(4, "tom", 54))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [tom, 54] at index 4\n     " +
                        "but: was given out-of-bounds row index: 4 (table only has 4 rows)");
    }

    @Test
    public void containsRowAtNegativeIndex_fails() {
        assertThatThrownBy(() -> assertThat(tableView).containsRowAtIndex(-1, "alice", 30))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [alice, 30] at index -1\n     " +
                        "but: was given negative row index: -1");
    }

    @Test
    public void containsRowAtIndex_wrong_types_fails() {
        assertThatThrownBy(() -> assertThat(tableView).containsRowAtIndex(1, 63, "deedee"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [63, deedee] at index 1\n     " +
                        "but: was [bob, 31] at index: 1");
    }

    @Test
    public void doesNotContainRowAtIndex() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(2);
        row3.put("name", "carol");
        row3.put("age", 42);

        Map<String, Object> row4 = new HashMap<>(2);
        row4.put("name", "dave");
        row4.put("age", 55);

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).doesNotContainRowAtIndex(0, "ebert", 49);
        assertThat(tableView).doesNotContainRowAtIndex(1, "alice", 30);
        assertThat(tableView).doesNotContainRowAtIndex(4, "granger", 49);
    }

    @Test
    public void doesNotContainRowAtIndex_with_empty_cells() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(1);
        row3.put("name", "carol");

        Map<String, Object> row4 = new HashMap<>(1);
        row4.put("name", "dave");

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).doesNotContainRowAtIndex(1, "alice", 30);
        assertThat(tableView).doesNotContainRowAtIndex(1, "bob", null);
        assertThat(tableView).doesNotContainRowAtIndex(2, null, 31);
        assertThat(tableView).doesNotContainRowAtIndex(2, "ebert", null);
        assertThat(tableView).doesNotContainRowAtIndex(2, null, null);
    }

    @Test
    public void doesNotContainRowAtIndex_fails() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(2);
        row3.put("name", "carol");
        row3.put("age", 42);

        Map<String, Object> row4 = new HashMap<>(2);
        row4.put("name", "dave");
        row4.put("age", 55);

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(tableView).doesNotContainRowAtIndex(0, "alice", 30))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [alice, 30] at index 0 to be false\n     " +
                        "but: was [alice, 30] at index: 0");
    }

    @Test
    public void containsRow() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(2);
        row3.put("name", "carol");
        row3.put("age", 42);

        Map<String, Object> row4 = new HashMap<>(2);
        row4.put("name", "dave");
        row4.put("age", 55);

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).containsRow("alice", 30);
        assertThat(tableView).containsRow("bob", 31);
        assertThat(tableView).containsRow("carol", 42);
        assertThat(tableView).containsRow("dave", 55);
    }

    @Test
    public void containsRow_with_empty_cells() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(1);
        row3.put("name", "carol");

        Map<String, Object> row4 = new HashMap<>(1);
        row4.put("name", "dave");

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).containsRow("alice", 30);
        assertThat(tableView).containsRow("bob", 31);
        assertThat(tableView).containsRow("carol", null);
        assertThat(tableView).containsRow("dave", null);
    }

    @Test
    public void containsRow_no_such_row_fails() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(1);
        row3.put("name", "carol");

        Map<String, Object> row4 = new HashMap<>(1);
        row4.put("name", "dave");

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(tableView).containsRow("jerry", 29))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [jerry, 29]\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void containsRow_wrong_types_fails() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(1);
        row3.put("name", "carol");

        Map<String, Object> row4 = new HashMap<>(1);
        row4.put("name", "dave");

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(tableView).containsRow(63, "deedee"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TableView has row: [63, deedee]\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void doesNotContainRow() {
        // given:
        Map<String, Object> row1 = new HashMap<>(2);
        row1.put("name", "alice");
        row1.put("age", 30);

        Map<String, Object> row2 = new HashMap<>(2);
        row2.put("name", "bob");
        row2.put("age", 31);

        Map<String, Object> row3 = new HashMap<>(2);
        row3.put("name", "carol");
        row3.put("age", 42);

        Map<String, Object> row4 = new HashMap<>(2);
        row4.put("name", "dave");
        row4.put("age", 55);

        Platform.runLater(() -> tableView.setItems(observableArrayList(row1, row2, row3, row4)));
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(tableView).doesNotContainRow("alice", 33);
        assertThat(tableView).doesNotContainRow("bobo", 31);
        assertThat(tableView).doesNotContainRow(null, 42);
        assertThat(tableView).doesNotContainRow("dave", null);
        assertThat(tableView).doesNotContainRow(null, null);
    }
}
