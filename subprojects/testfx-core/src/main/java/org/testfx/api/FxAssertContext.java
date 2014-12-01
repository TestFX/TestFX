package org.testfx.api;

import com.google.common.annotations.Beta;
import org.loadui.testfx.service.finder.NodeFinder;

@Beta
public class FxAssertContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private NodeFinder nodeFinder = FxService.serviceContext().getNodeFinder();

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public void setNodeFinder(NodeFinder nodeFinder) {
        this.nodeFinder = nodeFinder;
    }

}
