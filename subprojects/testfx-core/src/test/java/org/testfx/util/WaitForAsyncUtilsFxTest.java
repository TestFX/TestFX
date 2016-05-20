package org.testfx.util;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;

public class WaitForAsyncUtilsFxTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @BeforeClass
    public static void setUpClass(){
    	try {
			FxToolkit.registerPrimaryStage();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    }
	

    @Test(timeout=1000)
    public void asyncFx_callable_with_exception() throws Exception {
        // given:
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        Future<Void> future = WaitForAsyncUtils.asyncFx(callable);

        // expect:
        thrown.expectCause(instanceOf(UnsupportedOperationException.class));
        //WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForException(future);
    }
	
    protected void waitForException(Future<?> f) throws InterruptedException{
    	Thread.sleep(50);
        assertTrue(f.isDone());
    }
}
