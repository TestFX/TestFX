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

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComboBoxMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public ComboBox<String> comboBox;

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
            comboBox = new ComboBox<>();
            comboBox.setItems(observableArrayList("alice", "bob", "carol", "dave"));
            comboBox.getSelectionModel().selectFirst();
            return new StackPane(comboBox);
        });
        FxToolkit.showStage();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void hasItems() {
        // expect:
        assertThat(comboBox, ComboBoxMatchers.hasItems(4));
    }

    @Test
    public void hasItems_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox has 3 items\n");

        assertThat(comboBox, ComboBoxMatchers.hasItems(3));
    }

    @Test
    public void hasSelection() {
        assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("alice"));

        clickOn(".combo-box-base");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("bob"));
    }

    @Test
    public void hasSelection_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox has selection bob\n");

        assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("bob"));
    }

    @Test
    public void containsItems() {
        // expect:
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsItems("alice", "bob", "carol", "dave"));
        // not in order
        assertThat(comboBox, ComboBoxMatchers.containsItems("bob", "alice", "dave", "carol"));
        // partial
        assertThat(comboBox, ComboBoxMatchers.containsItems("bob", "alice", "dave"));
    }

    @Test
    public void containsItems_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox contains items [alice, bob, eric]\n");

        assertThat(comboBox, ComboBoxMatchers.containsItems("alice", "bob", "eric"));
    }

    @Test
    public void containsExactlyItems() {
        // expect:
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("alice", "bob", "carol", "dave"));
        // not in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("bob", "alice", "dave", "carol"));
    }

    @Test
    public void containsExactlyItems_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox contains exactly items [alice, bob, carol]\n");

        // missing "dave", so should fail
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("alice", "bob", "carol"));
    }

    @Test
    public void containsItemsInOrder() {
        // expect:
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("alice", "bob", "carol", "dave"));
        // partial (but in-order)
        assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("bob", "carol", "dave"));
    }

    @Test
    public void containsItemsInOrder_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox contains items in order [alice, carol, bob]\n");

        assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("alice", "carol", "bob"));
    }

    @Test
    public void containsExactlyItemsInOrder() {
        // expect:
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItemsInOrder("alice", "bob", "carol", "dave"));
    }

    @Test
    public void containsExactlyItemsInOrder_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: ComboBox contains exactly items in order [bob, alice, dave, carol]\n");

        // not in correct order, should fail
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItemsInOrder("bob", "alice", "dave", "carol"));
    }

}
