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
package org.testfx.util;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.testfx.api.FxToolkit;

public class WaitForAsyncUtilsFxTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);
    
    @BeforeClass
    public static void setUpClass(){
    	try {
			FxToolkit.registerPrimaryStage();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    }
	

    @Test
    public void asyncFx_callable_with_exception() throws Throwable {
        

    	WaitForAsyncUtils.printException=false;
        thrown.expectCause(instanceOf(UnsupportedOperationException.class));
        // given:
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        WaitForAsyncUtils.clearExceptions();
        Future<Void> future = WaitForAsyncUtils.asyncFx(callable);
        // expect:
        waitForException(future);
        //verify checkException
        try{WaitForAsyncUtils.checkException();assertTrue("checkException didn't detect Exception",false);}
        catch(Throwable e){
        	if(!(e instanceof UnsupportedOperationException)){ // should be!
        		throw e;
        	}
        }
        //verify exception in Future
    	WaitForAsyncUtils.printException=true;
        WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForThreads(future);
    }
	
    protected void waitForException(Future<?> f) throws InterruptedException{
    	Thread.sleep(50);
        assertTrue(f.isDone());
    }
    protected void waitForThreads(Future<?> f){
    	while(!f.isDone());
    	try{Thread.sleep(50);}
    	catch(Exception e){}
    }
    
}
