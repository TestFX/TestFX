/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package org.loadui.testfx.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

/**
 * Utilities to help with writing unit tests.
 *
 * @author dain.nilsson
 */
public class TestUtils
{
	public static void awaitCondition( Callable<Boolean> condition )
	{
		awaitCondition( condition, 5 );
	}

	public static void awaitCondition( Callable<Boolean> condition, int timeoutInSeconds )
	{
		long timeout = System.currentTimeMillis() + timeoutInSeconds * 1000;
		try
		{
			while( !condition.call() )
			{
				Thread.sleep( 10 );
				if( System.currentTimeMillis() > timeout )
				{
					throw new TimeoutException();
				}
			}
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}
	}
}
