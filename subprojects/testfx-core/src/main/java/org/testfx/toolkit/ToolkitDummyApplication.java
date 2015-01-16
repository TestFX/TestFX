package org.testfx.toolkit;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by jan on 15/01/15.
 */
public class ToolkitDummyApplication extends ToolkitApplication {

    public ToolkitDummyApplication() throws InstantiationException, IllegalAccessException {
        super();
    }

    @Override
    public Application getDelegate() {
        return new ToolkitDummyDelegate();
    }
}
