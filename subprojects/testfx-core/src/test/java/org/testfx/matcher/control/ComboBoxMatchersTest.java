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

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComboBoxMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule(2);

    ComboBox<String> comboBox;

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

    @Test
    public void hasItems() {
        assertThat(comboBox, ComboBoxMatchers.hasItems(4));
    }

    @Test
    public void hasItems_fails() {
        assertThatThrownBy(() -> assertThat(comboBox, ComboBoxMatchers.hasItems(3)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox has exactly 3 items\n     " +
                        "but: was 4");
    }

    @Test
    public void hasSelectedItem() {
        // given:
        assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("alice"));

        // when:
        clickOn(".combo-box-base");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // then:
        assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("bob"));
    }

    @Test
    public void hasSelectedItem_fails() {
        assertThatThrownBy(() -> assertThat(comboBox, ComboBoxMatchers.hasSelectedItem("bob")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox has selection \"bob\"\n     " +
                        "but: was \"alice\"");
    }

    @Test
    public void containsItems() {
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsItems("alice", "bob", "carol", "dave"));
        // not in order
        assertThat(comboBox, ComboBoxMatchers.containsItems("bob", "alice", "dave", "carol"));
        // partial
        assertThat(comboBox, ComboBoxMatchers.containsItems("bob", "alice", "dave"));
    }

    @Test
    public void containsItems_fails() {
        assertThatThrownBy(() -> assertThat(comboBox, ComboBoxMatchers.containsItems("alice", "bob", "eric")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox contains items [alice, bob, eric]\n" +
                        "     but: was [alice, bob, carol, dave]");
    }

    @Test
    public void containsExactlyItems() {
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("alice", "bob", "carol", "dave"));
        // not in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("bob", "alice", "dave", "carol"));
    }

    @Test
    public void containsExactlyItems_fails() {
        // missing "dave", so should fail
        assertThatThrownBy(() -> assertThat(comboBox, ComboBoxMatchers.containsExactlyItems("alice", "bob", "carol")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox contains exactly items [alice, bob, carol]\n" +
                        "     but: was [alice, bob, carol, dave]");
    }

    @Test
    public void containsItemsInOrder() {
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("alice", "bob", "carol", "dave"));
        // partial (but in-order)
        assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("bob", "carol", "dave"));
    }

    @Test
    public void containsItemsInOrder_fails() {
        assertThatThrownBy(() -> assertThat(comboBox, ComboBoxMatchers.containsItemsInOrder("alice", "carol", "bob")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox contains items in order [alice, carol, bob]\n" +
                        "     but: was [alice, bob, carol, dave]");
    }

    @Test
    public void containsExactlyItemsInOrder() {
        // in order
        assertThat(comboBox, ComboBoxMatchers.containsExactlyItemsInOrder("alice", "bob", "carol", "dave"));
    }

    @Test
    public void containsExactlyItemsInOrder_fails() {
        // not in correct order, should fail
        assertThatThrownBy(() -> assertThat(comboBox,
                ComboBoxMatchers.containsExactlyItemsInOrder("bob", "alice", "dave", "carol")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ComboBox contains exactly items in order [bob, alice, dave, carol]\n" +
                        "     but: was [alice, bob, carol, dave]");
    }

}
