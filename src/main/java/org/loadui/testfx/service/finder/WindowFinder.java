package org.loadui.testfx.service.finder;

import java.util.List;
import javafx.stage.Window;

public interface WindowFinder {
    public Window getLastTargetWindow();
    public void setLastTargetWindow(Window window);

    public List<Window> listWindows();
    public List<Window> listOrderedWindows();
}
