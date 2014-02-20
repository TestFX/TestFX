package org.loadui.testfx.exceptions;

public abstract class NodeQueryException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NodeQueryException(String message)
    {
        super(message);
    }
}
