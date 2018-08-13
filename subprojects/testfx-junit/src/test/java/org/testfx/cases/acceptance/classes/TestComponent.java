package org.testfx.cases.acceptance.classes;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TestComponent extends VBox {

    public MouseEvent btnEvent;

    public TestComponent() {
        Button button = new Button("Button");
        button.setId("button");
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> btnEvent = e);
        TextField text = new TextField("A textfield");
        getChildren().addAll(button, text);
    }

}
