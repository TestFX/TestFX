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
package org.testfx.cases.integration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.robot.Motion;

import static org.hamcrest.CoreMatchers.is;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.DebugUtils.informedErrorMessage;

public class MenuBarTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule(2);

    FxRobot fxRobot = new FxRobot();
    Menu editMenu;
    CountDownLatch newMenuShownLatch = new CountDownLatch(1);
    CountDownLatch editMenuShownLatch = new CountDownLatch(1);

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage(stage -> {
            Menu fileMenu = new Menu("File");
            fileMenu.setId("fileMenu");

            MenuItem newItem = new MenuItem("New");
            newItem.setId("newItem");
            newItem.setOnAction(actionEvent -> newMenuShownLatch.countDown());
            fileMenu.getItems().add(newItem);

            MenuItem saveAsItem = new MenuItem("Save As..............................");
            saveAsItem.setId("saveAsItem");
            saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN,
                    KeyCombination.CONTROL_DOWN));
            fileMenu.getItems().add(saveAsItem);

            editMenu = new Menu("Edit");
            editMenu.setId("editMenu");

            MenuItem cutItem = new MenuItem("cut");
            cutItem.setId("cutItem");
            editMenu.getItems().add(cutItem);

            MenuBar menuBar = new MenuBar(fileMenu, editMenu);
            StackPane pane = new StackPane(new VBox(menuBar));
            pane.setAlignment(Pos.CENTER);
            Scene scene = new Scene(pane, 300, 400);
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * When moving directly from "#fileMenu" to "#newItem" (i.e. diagonally) the {@code editMenu}
     * is activated because of the width of {@code fileMenu}. However we detect this scenario and
     * instead move vertically (along the y-axis) first, and then horizontally (along the x-axis).
     *
     * @see <a href="https://github.com/TestFX/TestFX/issues/309">Issue #309</a>
     */
    @Test
    public void should_move_vertically_first() throws Exception {
        // First we show that it is indeed the case that {@code editMenu} is triggered when moving directly:
        editMenu.setOnShown(event -> editMenuShownLatch.countDown());
        fxRobot.clickOn("#fileMenu").clickOn("#newItem", Motion.DIRECT);
        verifyThat(editMenuShownLatch.await(3, TimeUnit.SECONDS), is(true), informedErrorMessage(fxRobot));
        verifyThat(newMenuShownLatch.getCount(), is(1L), informedErrorMessage(fxRobot));

        // Next we show that calling the "clickOn" method without specifying the type of motion automatically
        // uses Motion.VERTICAL_FIRST because "#newItem" is a MenuItem.
        fxRobot.clickOn("#fileMenu").clickOn("#newItem");
        verifyThat(newMenuShownLatch.await(3, TimeUnit.SECONDS), is(true), informedErrorMessage(fxRobot));
    }
}
