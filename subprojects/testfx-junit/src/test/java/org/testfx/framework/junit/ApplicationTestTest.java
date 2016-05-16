/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */

package org.testfx.framework.junit;

import static org.junit.Assert.assertTrue;

import org.junit.AssumptionViolatedException;
import org.junit.Test;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class for testing features of {@link ApplicationTest}.
 * 
 */
public class ApplicationTestTest {

	/**
	 * Tests that the application runs with doUITest set to true.
	 */
	@Test public void shouldRunTest(){
		DutClass dut=new DutClass();
		assertTrue(!dut.started);
		ApplicationTest.doUITest=true;
		try{
			dut.internalBefore();
			assertTrue(dut.started);
			dut.internalAfter();
		}catch(Exception e){
			assertTrue(false);
		}
	}

	/**
	 * Tests that the application does not run with doUITest set to false.
	 */
	@Test public void shouldNotRunTest(){
		DutClass dut=new DutClass();
		assertTrue(!dut.started);
		ApplicationTest.doUITest=false;
		try{
			dut.internalBefore();
			assertTrue(!dut.started);
			dut.internalAfter();
		}catch(Exception e){
			if(!(e instanceof AssumptionViolatedException)){
				assertTrue(false);
			}
		}
	}
	
	
	protected class DutClass extends ApplicationTest{

		public boolean started=false;
		
		
		@Override
		public void start(Stage stage) throws Exception {
			started=true;
			Pane p=new Pane();
			p.setId("dutPane");
	        // setup scene and stage.
	        Scene scene = new Scene(p, 200, 200);
	        stage.setScene(scene);
	        stage.show();
		}
		
	}
}
