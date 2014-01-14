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
package org.loadui.testfx;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface ScreenController
{
	public Point2D getMouse();

	public void position( double x, double y );

	public void move( double x, double y );

	public void scroll( int amount );

	public void press( MouseButton button );

	public void release( MouseButton button );

	public void press( KeyCode key );

	public void release( KeyCode key );
	
	public void pressNoWait( KeyCode key );

    public void releaseNoWait( KeyCode key );

}
