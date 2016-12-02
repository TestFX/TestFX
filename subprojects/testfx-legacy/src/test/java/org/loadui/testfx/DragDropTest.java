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

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBoxBuilder;

import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.ListViews.containsRow;
import static org.loadui.testfx.controls.ListViews.numberOfRowsIn;

public class DragDropTest extends GuiTest {

    /**
     * A typical event handler to start a drag-drop operation.
     */
    private final EventHandler<MouseEvent> onDragDetected = event -> {
        Object draggedItem = ((ListView<?>) event.getSource()).getSelectionModel().getSelectedItem();
        ClipboardContent content = new ClipboardContent();
        content.putString(draggedItem.toString());

        Dragboard dragboard = ((Node) event.getSource()).startDragAndDrop(TransferMode.MOVE);
        dragboard.setContent(content);
        event.consume();
    };
    /**
     * A typical event handler to delete the source item from the source list after the drag-drop operation completed.
     */
    private final EventHandler<DragEvent> onDragDone = event -> {
        ListView<?> listView = (ListView<?>) event.getSource();
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
    };
    /**
     * A typical event handler to accept drag events.
     */
    private final EventHandler<DragEvent> onDragOver = event -> event.acceptTransferModes(TransferMode.MOVE);
    /**
     * A typical event handler to handle drop events.
     */
    private final EventHandler<DragEvent> onDragDropped = event -> {
        event.acceptTransferModes(TransferMode.MOVE);
        String item = event.getDragboard().getString();
        if (item != null) {
            @SuppressWarnings("unchecked")
            ListView<String> listView = (ListView<String>) event.getSource();
            listView.getItems().add(item);
            event.setDropCompleted(true);
            event.consume();
        }
    };
    private ListView<String> list1;
    private ListView<String> list2;

    @Override
    protected Parent getRootNode() {
        list1 = new ListView<>();
        list2 = new ListView<>();

        list1.setOnDragDetected(onDragDetected);
        list2.setOnDragDetected(onDragDetected);
        list1.setOnDragDone(onDragDone);
        list2.setOnDragDone(onDragDone);
        list1.setOnDragOver(onDragOver);
        list2.setOnDragOver(onDragOver);
        list1.setOnDragEntered(onDragOver);
        list2.setOnDragEntered(onDragOver);
        list1.setOnDragDropped(onDragDropped);
        list2.setOnDragDropped(onDragDropped);

        return HBoxBuilder.create().children(list1, list2).build();
    }

    @Test(timeout = 10000)
    public void shouldMoveElements() throws Exception {

        FXTestUtils.invokeAndWait(() -> {
            list1.getItems().addAll("A", "B", "C");
            list2.getItems().addAll("X", "Y", "Z");
        }, 1000);

        verifyThat(numberOfRowsIn(list1), is(3));
        verifyThat(numberOfRowsIn(list2), is(3));

        drag("A").dropTo("X");
        verifyThat(numberOfRowsIn(list1), is(2));
        verifyThat(numberOfRowsIn(list2), is(4));
        verifyThat(list2, containsRow("A"));
        verifyThat(list1, not(containsRow("A")));

        drag("Z").dropTo("B");
        drag("C").dropTo("B"); // Should have no effect
        verifyThat(list1.getItems().size(), is(3));
        verifyThat(list2.getItems().size(), is(3));
        verifyThat(list1.getItems(), hasItems("Z", "B", "C"));
        verifyThat(list2.getItems(), hasItems("A", "X", "Y"));
    }

}
