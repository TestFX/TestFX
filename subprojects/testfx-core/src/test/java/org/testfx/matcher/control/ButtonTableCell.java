/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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

import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * <p>A {@link TableCell} for use in matcher tests. It is somehwat special, because it does contain a {@link Button}
 * rather than a real value.
 * </p>
 * <p>Implementation has been inspired by: 
 * @see <a href="https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view">https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view</a>
 * </p>
 */
final class ButtonTableCell extends TableCell<Map, Button> {

    private final Button button;

    public static Callback<TableColumn<Map, Button>, TableCell<Map, Button>> forTableColumn(String buttonLabel,
            Consumer<Map> action) {
        return param -> new ButtonTableCell(buttonLabel, action);
    }

    private ButtonTableCell(String buttonLabel, Consumer<Map> action) {
        button = new Button(buttonLabel);
        button.setOnAction(actionEvent -> {
            action.accept(getCurrentModel());
        });
        button.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (button.getId() == null) {
                button.setId(new Random().nextInt() + "");
            }
            setGraphic(button);
        }
    }

    private Map getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
