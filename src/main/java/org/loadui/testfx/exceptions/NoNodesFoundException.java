package org.loadui.testfx.exceptions;

public class NoNodesFoundException extends NodeQueryException
{
    private static final long serialVersionUID = 1L;

    public NoNodesFoundException(String message)
	{
		super(message);
	}
}
