package org.testfx.framework.junit;

import static org.junit.Assert.assertTrue;

import org.junit.AssumptionViolatedException;
import org.junit.Test;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationTestTest {

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
