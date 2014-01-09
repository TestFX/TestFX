package org.loadui.testfx.exceptions;

public class NoNodesVisibleException extends NodeQueryException {
    private static final long serialVersionUID = 1L;

    public NoNodesVisibleException(String message)
    {
        super(message);
    }
}
