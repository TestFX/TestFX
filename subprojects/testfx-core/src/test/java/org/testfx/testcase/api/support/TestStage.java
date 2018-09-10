package org.testfx.testcase.api.support;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestStage extends Stage {

    TestComponent box;

    public TestStage() {
        box = new TestComponent();
        Scene s = new Scene(box);
        setScene(s);
    }

    public TestComponent getTestBox() {
        return box;
    }

}
