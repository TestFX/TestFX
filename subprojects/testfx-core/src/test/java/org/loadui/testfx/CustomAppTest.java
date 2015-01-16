package org.loadui.testfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomAppTest extends ApplicationTest {

    @Override
    protected Class<? extends Application> getApplicationClass() {
        return CustomApp.class;
    }

    public static class CustomApp extends Application {

        private String labelText = "";

        @Override
        public void init() throws Exception {
            labelText = "Test";
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            Label label = new Label(labelText);
            label.setId("label");

            StackPane root = new StackPane();
            root.getChildren().add(label);

            Scene scene = new Scene(root, 300, 250);

            primaryStage.setTitle("New stage");
            primaryStage.setScene(scene);
        }
    }

    @Test
    @Ignore("Test cannot be run with other tests as it creates a new app")
    public void testApplicationStart() throws InterruptedException {
        Label label = find("#label");

        assertEquals(label.getText(), "Test");
    }

}
