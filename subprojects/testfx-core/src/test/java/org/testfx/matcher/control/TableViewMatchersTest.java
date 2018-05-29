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
package org.testfx.matcher.control;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableViewMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule(2);

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
            tableColumn0.setId("tableColumn0");
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
        assertThat(tableView, TableViewMatchers.hasTableCell("alice"));
        assertThat(tableView, TableViewMatchers.hasTableCell("bob"));
    }

    @Test
    public void hasTableCell_customCellValueFactory() {
        // given:
        Platform.runLater(() -> tableColumn0.setCellFactory(column -> new TableCell<Map, String>() {
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

        // expect:
        assertThat(tableView, TableViewMatchers.hasTableCell("ALICE!"));
        assertThat(tableView, TableViewMatchers.hasTableCell("BOB!"));
    }

    @Test
    public void hasTableCell_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.hasTableCell("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has table cell \"foobar\"\n     " +
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

        // expect:
        // FIXME(mike): Currently the table is printed without applying the cell value factory
        // once that is fixed, add these lines:
        // "but: was [[ALICE!, 30], [BOB!, 31], [CAROL!, null], [DAVE!, null]]");
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.hasTableCell("ALICE!!!")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: TableView has table cell \"ALICE!!!\"\n     ");
    }

    @Test
    public void hasTableCell_with_toString() {
        assertThat(tableView, TableViewMatchers.hasTableCell("30"));
        assertThat(tableView, TableViewMatchers.hasTableCell(31));
    }

    @Test
    public void hasTableCell_with_null_fails() {
        // FIXME: This works but it is nonsensical - why can't we accept null?
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.hasTableCell(null)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has table cell \"null\"\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    @Test
    public void hasNumRows() {
        assertThat(tableView, TableViewMatchers.hasNumRows(4));
    }

    @Test
    public void hasNumRows_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.hasNumRows(0)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has 0 rows\n     " +
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
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(0, "alice", 30));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(1, "bob", 31));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(2, "carol", null));
        assertThat(tableView, TableViewMatchers.containsRowAtIndex(3, "dave", null));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(0, "ebert", null)));
        assertThat(tableView, not(TableViewMatchers.containsRowAtIndex(3, "carol", null)));
    }

    @Test
    public void containsRowAtIndex_no_such_row_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRowAtIndex(0, "jerry", 29)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [jerry, 29] at index 0\n     " +
                        "but: was [alice, 30] at index: 0");
    }

    @Test
    public void containsRowAtIndex_out_of_bounds_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRowAtIndex(4, "tom", 54)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [tom, 54] at index 4\n     " +
                        "but: was given out-of-bounds row index: 4 (table only has 4 rows)");
    }

    @Test
    public void containsRowAtNegativeIndex_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRowAtIndex(-1, "alice", 30)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [alice, 30] at index -1\n     " +
                        "but: was given negative row index: -1");
    }

    @Test
    public void containsRowAtIndex_wrong_types_fails() {
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRowAtIndex(1, 63, "deedee")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [63, deedee] at index 1\n     " +
                        "but: was [bob, 31] at index: 1");
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
        assertThat(tableView, TableViewMatchers.containsRow("alice", 30));
        assertThat(tableView, TableViewMatchers.containsRow("bob", 31));
        assertThat(tableView, TableViewMatchers.containsRow("carol", 42));
        assertThat(tableView, TableViewMatchers.containsRow("dave", 55));
        assertThat(tableView, not(TableViewMatchers.containsRow("ebert", 49)));
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
        assertThat(tableView, TableViewMatchers.containsRow("alice", 30));
        assertThat(tableView, TableViewMatchers.containsRow("bob", 31));
        assertThat(tableView, TableViewMatchers.containsRow("carol", null));
        assertThat(tableView, TableViewMatchers.containsRow("dave", null));
        assertThat(tableView, not(TableViewMatchers.containsRow("ebert", null)));
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
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRow("jerry", 29)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [jerry, 29]\n     " +
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
        assertThatThrownBy(() -> assertThat(tableView, TableViewMatchers.containsRow(63, "deedee")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TableView has row: [63, deedee]\n     " +
                        "but: was [[alice, 30], [bob, 31], [carol, null], [dave, null]]");
    }

    /**
     * @see <a href="https://github.com/TestFX/TestFX/issues/541">Issue #541</a>
     */
    @Test
    public void shouldQueryTableHeader() {
        assertThat(lookup("#tableColumn0").query().getStyleClass(), hasItem("column-header"));
        assertThat(lookup("#tableColumn0").tryQuery().get().getStyleClass(), hasItem("column-header"));
    }

    @Test
    @Ignore("Issue #449")
    public void containsRow_after_edited_cell() throws TimeoutException {
        // given:
        TableColumn<Person, String> tableColumn0 = new TableColumn<>("name");
        tableColumn0.setEditable(true);
        tableColumn0.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn0.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumn0.setPrefWidth(150);
        TableColumn<Person, Number> tableColumn1 = new TableColumn<>("age");
        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("age"));
        tableColumn1.setEditable(true);
        TableView<Person> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.getColumns().setAll(tableColumn0, tableColumn1);
        Person alice = new Person("alice", 30);
        Person bob = new Person("bob", 41);
        tableView.setItems(observableArrayList(alice, bob));
        FxToolkit.setupSceneRoot(() -> new StackPane(tableView));
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("alice");
        clickOn("alice");
        press(KeyCode.BACK_SPACE);
        write("not alice!");
        press(KeyCode.ENTER);

        // then:
        assertThat(tableView, TableViewMatchers.containsRow("not alice!", 30));
    }

    public static class Person {
        private final StringProperty name;
        private final IntegerProperty age;

        public Person(String name, int age) {
            this.name = new SimpleStringProperty(name);
            this.age = new SimpleIntegerProperty(age);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public IntegerProperty ageProperty() {
            return age;
        }
    }
}
