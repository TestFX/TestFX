package org.testfx.api;

import com.google.common.annotations.Beta;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.WindowFinder;
import org.loadui.testfx.service.finder.impl.NodeFinderImpl;
import org.loadui.testfx.service.finder.impl.WindowFinderImpl;

@Beta
public class FxSelectorContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private WindowFinder windowFinder = new WindowFinderImpl();

    private NodeFinder nodeFinder = new NodeFinderImpl(windowFinder);

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public WindowFinder getWindowFinder() {
        return windowFinder;
    }

    public void setWindowFinder(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
    }

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public void setNodeFinder(NodeFinder nodeFinder) {
        this.nodeFinder = nodeFinder;
    }

}
