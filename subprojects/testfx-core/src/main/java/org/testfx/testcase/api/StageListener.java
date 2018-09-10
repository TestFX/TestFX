package org.testfx.testcase.api;

import javafx.stage.Stage;

public class StageListener {

    protected boolean visible;
    
    
    public StageListener(Stage s) {
        s.setOnShown(e -> visible = true);
        //s.setOnHidden(e -> visible = false);
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    
}
