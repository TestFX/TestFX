package org.loadui.testfx.service.finder;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Window;

public interface WindowFinder {
    public Window getLastTargetWindow();
    public void setLastTargetWindow(Window window);

    public List<Window> listWindows();
    public List<Window> listOrderedWindows();

    public Window window(int windowNumber);
    public Window window(String stageTitleRegex);
    public Window window(Scene scene);
}
