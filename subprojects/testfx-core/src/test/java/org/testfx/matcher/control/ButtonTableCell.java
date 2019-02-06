package org.testfx.matcher.control;

import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * <p>A {@link TableCell} for use in matcher tests. It is somehwat special, because it does contain a {@link Button} rather than a real value.</p>
 * <p>Implementation has been inspired by: 
 * @see <a href="https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view">https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view</a>.
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
                button.setId(new Random().nextInt()+"");
            }
            setGraphic(button);
        }
    }

    private Map getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
