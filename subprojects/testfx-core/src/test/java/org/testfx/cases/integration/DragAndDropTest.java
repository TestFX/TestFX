/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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

import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.TestFXRule;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.Matchers.contains;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.DebugUtils.informedErrorMessage;

public class DragAndDropTest extends TestCaseBase {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();
    public ListView<String> leftListView;
    public ListView<String> rightListView;

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            leftListView = new ListView<>(observableArrayList("L1", "L2", "L3"));
            rightListView = new ListView<>(observableArrayList("R1", "R2", "R3"));
            Lists.newArrayList(leftListView, rightListView).forEach(this::setupListView);
            return new HBox(leftListView, rightListView);
        });
        FxToolkit.setupStage(Stage::show);
    }

    public void setupListView(ListView<String> listView) {
        listView.setOnDragDetected(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedItem);
            Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
            dragboard.setContent(content);
            event.consume();
        });

        listView.setOnDragEntered(event -> event.acceptTransferModes(TransferMode.MOVE));
        listView.setOnDragOver(event -> event.acceptTransferModes(TransferMode.MOVE));

        listView.setOnDragDropped(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Dragboard dragboard = event.getDragboard();
            String acceptedItem = dragboard.getString();
            if (acceptedItem != null) {
                listView.getItems().add(acceptedItem);
                event.setDropCompleted(true);
                event.consume();
            }
        });

        listView.setOnDragDone(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            listView.getItems().remove(selectedItem);
        });
    }

    @Test
    public void should_have_initialized_items() {
        // expect:
        verifyThat(leftListView.getItems(), contains("L1", "L2", "L3"), informedErrorMessage(this));
        verifyThat(rightListView.getItems(), contains("R1", "R2", "R3"), informedErrorMessage(this));
    }

    @Test
    public void should_drag_and_drop_from_left_to_right() {
        // when:
        drag("L1");
        moveTo("R1");
        drop();

        // then:
        verifyThat(leftListView.getItems(), contains("L2", "L3"), informedErrorMessage(this));
        // gets added to end of ListView
        verifyThat(rightListView.getItems(), contains("R1", "R2", "R3", "L1"), informedErrorMessage(this));
    }

    @Test
    public void should_drag_and_drop_from_right_to_left() {
        // when:
        drag("R3");
        dropTo("L2");
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(leftListView.getItems(), contains("L1", "L2", "L3", "R3"), informedErrorMessage(this));
        verifyThat(rightListView.getItems(), contains("R1", "R2"), informedErrorMessage(this));
    }

    @Ignore("see #383")
    @Test
    public void should_drag_and_drop_from_left_to_left() {
        // when:
        drag("L3");
        dropTo("L2");
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(leftListView.getItems(), contains("L1", "L2", "L3"), informedErrorMessage(this));
        verifyThat(rightListView.getItems(), contains("R1", "R2", "R3"), informedErrorMessage(this));
    }

}
