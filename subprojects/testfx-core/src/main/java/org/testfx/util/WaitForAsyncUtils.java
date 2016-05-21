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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SettableFuture;
import org.testfx.api.annotation.Unstable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Provides static methods for handling the execution on different Threads.
 * The Test Thread is usually running on the main thread, while the GUI runs
 * on the FXApplication Thread. Additionally tasks may also be started on
 * different asynchronous threads.
 * <p>
 * General convention:
 * <ul>
 * <li> {@code async} without suffix refers to a unknown Thread in a ThreadPool
 * <li> the suffix {@code Fx} refers to the FX application thread
 * </ul>
 * <p>
 * <strong>Exception handling</strong><br>
 * As exceptions on different threads are computed in those threads, the caller
 * is usually not aware of those exceptions. Exceptions returned directly from this Framework
 * are wrapped in {@code RuntimeExceptions}.<br>
 * There are two way this class notifies the user of exceptions:
 * <ul><li> the returned future
 * <li> the internal exception stack
 * </ul><p>
 * Usually exceptions are forwarded in the returned <b>Future</b> of the Methods in this class.
 * When the calling Thread calls the getter of the Future, the exceptions will be thrown.
 * All {@code waitFor} methods acquire the value of the future and will throw those
 * exceptions too.<br>
 * The <b>internal exception stack</b> stores the unhandled exceptions of direct calls to the {@code async} methods.
 * As this class can not guarantee, that exceptions in these methods are handled properly,
 * it will internally store those exceptions too. The exceptions will be in the stack, unit they 
 * are handled somewhere in the Application. If the field {@code autoCheckException} is set to true,
 * any subsequent call to one of the {@code async} methods will throw one of those exceptions.
 * 
 *
 */
@Unstable
public class WaitForAsyncUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private final static long CONDITION_SLEEP_IN_MILLIS = 10;

    private final static long SEMAPHORE_SLEEP_IN_MILLIS = 10;

    private final static int SEMAPHORE_LOOPS_COUNT = 5;

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    //private static WaitForAsyncUtilsExecutor executorService = new WaitForAsyncUtilsExecutor();
    private static List<Throwable> exceptions=Collections.synchronizedList(new ArrayList<Throwable>());

    /** If true, the exceptions during any of the {@code async} methods will be printed to stderr. **/
    public static boolean printException=true;
    /** If set to true, any call of the {@code async} methods will check for unhandled exceptions. **/
    public static boolean autoCheckException=true;
    
    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // ASYNC METHODS.

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> async(Runnable runnable) {
    	if(autoCheckException)checkExceptionWrapped();
    	Callable<Void> call=new ASyncFXCallable<Void>(runnable, true);
    	return executorService.submit(call);
    }
    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     *
     * @param runnable the runnable
     * @param throwExceptions if true, exceptions will be thrown on the executing thread
     * @return a future
     */
    public static Future<Void> async(Runnable runnable, boolean throwExceptions) {
    	if(autoCheckException)checkExceptionWrapped();
    	Callable<Void> call=new ASyncFXCallable<Void>(runnable,throwExceptions);
    	return executorService.submit(call);
    }

    /**
     * Calls the given {@link Callable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     * 
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> async(Callable<T> callable) {
    	if(autoCheckException)checkExceptionWrapped();
    	ASyncFXCallable<T> call=new ASyncFXCallable<T>(callable,true);
    	executorService.submit((Runnable)call); //exception handling not guaranteed
    	return call;
    }
    /**
     * Calls the given {@link Callable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     *
     * @param callable the callable
     * @param throwExceptions if true, exceptions will be thrown on the executing thread
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> async(Callable<T> callable, boolean throwExceptions) {
    	if(autoCheckException)checkExceptionWrapped();
    	Callable<T> call=new ASyncFXCallable<T>(callable,throwExceptions);
    	return executorService.submit(call); //exception handling not guaranteed
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> asyncFx(Runnable runnable) {
    	if(autoCheckException)checkExceptionWrapped();
    	ASyncFXCallable<Void> call = new ASyncFXCallable<Void>(runnable,true);//;Executors.callable(runnable, null);
        runOnFxThread(call);
        return call;
    }


    /**
     * Calls the given {@link Callable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error. <br>
     * You need to evaluate the returned Future ({@link Future#get()}) 
     * for exceptions or call the {@link #checkException()} method to handle Exceptions, after the
     * task has finished.
     *
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> asyncFx(Callable<T> callable) {
    	if(autoCheckException)checkExceptionWrapped();
    	ASyncFXCallable<T> call = new ASyncFXCallable<T>(callable,true);//;Executors.callable(runnable, null);
        runOnFxThread(call);
        return call;
    }

    // WAIT-FOR METHODS.

    /**
     * Waits for given {@link Future} to be set (push) and returns {@code T}.
     * @param future the future
     * @param <T> the future type
     * @return a result
     */
    public static <T> T waitFor(Future<T> future) {
        try {
            return future.get();
        }
        catch (ExecutionException exception) {
            // if the computation threw an exception.
            throw new RuntimeException(exception.getCause());
        }
        catch (InterruptedException ignore) {
            // if the current thread was interrupted while waiting.
            return null;
        }
    }
    

    /**
     * Waits for given {@link Future} to be set (push) and returns {@code T}, otherwise times out
     * with {@link TimeoutException}.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param future the future
     * @param <T> the future type
     * @return a result
     * @throws TimeoutException
     */
    public static <T> T waitFor(long timeout,
                                TimeUnit timeUnit,
                                Future<T> future)
                         throws TimeoutException {
        try {
            return future.get(timeout, timeUnit);
        }
        catch (ExecutionException exception) {
            // if the computation threw an exception.
            throw new RuntimeException(exception.getCause());
        }
        catch (InterruptedException ignore) {
            // if the current thread was interrupted while waiting.
            return null;
        }
    }

    /**
     * Waits for given condition {@link Callable} to return (pull) {@code true}, otherwise times
     * out with {@link TimeoutException}. The condition will be evaluated at least once. The method
     * will wait for the last condition to finish after a timeout.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param condition the condition
     * @throws TimeoutException
     */
    public static void waitFor(long timeout,
                               TimeUnit timeUnit,
                               Callable<Boolean> condition)
                        throws TimeoutException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (!callConditionAndReturnResult(condition)) {
            sleep(CONDITION_SLEEP_IN_MILLIS, MILLISECONDS);
            if (stopwatch.elapsed(timeUnit) > timeout) {
                throw new TimeoutException();
            }
        }
    }

    /**
     * Waits for given observable {@link ObservableBooleanValue} to return (push) {@code true},
     * otherwise times out with {@link TimeoutException}.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param booleanValue the observable
     * @throws TimeoutException
     */
    public static void waitFor(long timeout,
                               TimeUnit timeUnit,
                               ObservableBooleanValue booleanValue)
                        throws TimeoutException {
        SettableFuture<Void> future = SettableFuture.create();
        ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                future.set(null);
            }
        };
        booleanValue.addListener(changeListener);
        if (!booleanValue.get()) {
            waitFor(timeout, timeUnit, future);
        }
        booleanValue.removeListener(changeListener);
    }

    // WAIT-FOR-FX-EVENTS METHODS.

    /**
     * Waits for the event queue of JavaFX Application Thread to be completed, as well as any new
     * events triggered in it.
     */
    public static void waitForFxEvents() {
        waitForFxEvents(SEMAPHORE_LOOPS_COUNT);
    }

    /**
     * Waits the given {@code int} attempts for the event queue of JavaFX Application Thread to be
     * completed, as well as any new events triggered on it.
     *
     * @param attemptsCount the attempts
     */
    public static void waitForFxEvents(int attemptsCount) {
        for (int attempt = 0; attempt < attemptsCount; attempt++) {
            blockFxThreadWithSemaphore();
            sleep(SEMAPHORE_SLEEP_IN_MILLIS, MILLISECONDS);
        }
    }

    // SLEEP METHODS.

    /**
     * Sleeps the given duration.
     *
     * @param duration the duration
     * @param timeUnit the time unit
     */
    public static void sleep(long duration,
                             TimeUnit timeUnit) {
        try {
            sleepWithException(duration, timeUnit);
        }
        catch (InterruptedException ignore) {}
    }

    /**
     * Sleeps the given duration.
     *
     * @param duration the duration
     * @param timeUnit the time unit
     * @throws InterruptedException
     */
    public static void sleepWithException(long duration,
                                          TimeUnit timeUnit)
                                   throws InterruptedException {
        Thread.sleep(timeUnit.toMillis(duration));
    }

    // WAIT-FOR-ASYNC METHODS.

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and waits for it {@code long}
     * milliseconds to finish, otherwise times out with {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     */
    public static void waitForAsync(long millis,
                                    Runnable runnable) {
        Future<Void> future = async(runnable, false); //exceptions handled in wait --> safe
        waitForMillis(millis, future);
    }

    /**
     * Calls the given {@link Callable} on a new {@link Thread}, waits for it {@code long}
     * milliseconds to finish and returns {@code T}, otherwise times out with
     * {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param callable the callable
     * @param <T> the callable type
     * @return a result
     */
    public static <T> T waitForAsync(long millis,
                                     Callable<T> callable) {
        Future<T> future = async(callable, false); //exceptions handled in wait --> safe
        return waitForMillis(millis, future);
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and waits for it {@code long} milliseconds to finish, otherwise times out with
     * {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     */
    public static void waitForAsyncFx(long millis,
                                      Runnable runnable) {
        Future<Void> future = asyncFx(runnable);
        waitForMillis(millis, future);
    }

    /**
     * Calls the given {@link Callable} on the JavaFX Application Thread at some unspecified time
     * in the future, waits for it {@code long} milliseconds to finish and returns {@code T},
     * otherwise times out with {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param callable the callable
     * @param <T> the callable type
     * @return a result
     */
    public static <T> T waitForAsyncFx(long millis,
                                       Callable<T> callable) {
        Future<T> future = asyncFx(callable);
        return waitForMillis(millis, future);
    }

    
    /**
     * Checks if a exception in a async task occurred, that has not been checked currently.
     * If so, the first exception will be removed and thrown by this method.
     * 
     * @throws Throwable the exception
     */
    public static void checkException() throws Throwable{
    	Throwable t=getCheckException();
    	if(t!=null)throw t;
    }
    
    /**
     * Prints currently unhandled exceptions to stderr. The exceptions are not removed 
     * from the stack.
     */
    public static void printExceptions(){
    	for(int i=0;i<exceptions.size();i++){
    		System.err.println("ExceptionQueue: "+i);
    		exceptions.get(i);
    	}
    }
    /**
     * Clears all currently unhandled exceptions.
     */
    public static void clearExceptions(){
    	exceptions.clear();
    }
    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * Internal function that allows throws Exceptions. It does not require to handle the Exceptions.
     */
    private static void checkExceptionWrapped(){
    	Throwable t=getCheckException();
    	if(t instanceof RuntimeException)
    		throw (RuntimeException)t;
    	else if(t instanceof Error)
    		throw (Error)t;
    }
    /**
     * Pops a exception from the stack and adds an entry in the stack trace, to notify the user, that
     * this is not the original place of the exception.
     * @return the exception or null if none in stack
     */
    private static Throwable getCheckException(){
    	if(exceptions.size()>0){
    		Throwable t=exceptions.get(0);
    		StackTraceElement se=new StackTraceElement(WaitForAsyncUtils.class.getName(),"----DelayedExceptionSeeTraceBelow----",WaitForAsyncUtils.class.getSimpleName()+".java",0);
    		StackTraceElement[] st=new StackTraceElement[1];
    		st[0]=se;
    		t.setStackTrace(st);
    		exceptions.remove(0);
    		return t;
    	}
    	return null;
    }
    
    private static <T> T waitForMillis(long millis,
                                       Future<T> future) {
        try {
            return waitFor(millis, MILLISECONDS, future); //exceptions are thrown on current thread
        }
        catch (TimeoutException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void runOnFxThread(Runnable runnable) {
        //Platform.runLater(runnable);
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        }
        else {
            Platform.runLater(runnable);
        }
    }
    
    
    private static boolean callConditionAndReturnResult(Callable<Boolean> condition) {
        try {
            return condition.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void blockFxThreadWithSemaphore() {
        Semaphore semaphore = new Semaphore(0);
        runOnFxThread(semaphore::release);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ignore) {}
    }
    
    
    
    /**
     * Internally used callable, that handles all the async stuff. All external callables/runnables
     * must be wrapped in this class. Then, this class is executed on the other task. <br>
     * <b>This is a single call object. Do not use twice!</b>
     *
     * @param <X> the return type of the callable
     */
    private static class ASyncFXCallable<X> extends FutureTask<X> implements Callable<X>{

    	/** If true, exceptions will be added to the internal stack **/
    	private final boolean throwException;
    	/** Holds the stacktrace of the caller, for printing, if an Exception occurs **/
    	private final StackTraceElement[] trace;
    	private boolean running=false;
    	/** The unhandled exception **/
    	private Throwable exception=null;
    	
    	public ASyncFXCallable(Runnable r, boolean throwException){
    		super(r, (X)null);
    		this.throwException=throwException;
    		trace=Thread.currentThread().getStackTrace();
    	}
    	public ASyncFXCallable(Callable<X> r, boolean throwException){
    		super(r);
    		this.throwException=throwException;
    		trace=Thread.currentThread().getStackTrace(); //Exception?!
    	}
    	
    	/**
    	 * Runs the task, evaluates exceptions during execution. 
    	 * Exceptions are printed and pushed on the stack.
    	 */
    	@Override
    	public void run(){
			running=true;
    		super.run();
    		try{
    			get();
    		}catch(Exception e){
    			if(throwException){
    				if(printException){
	    				String out="--- Exception in Async Thread ---\n";
	    				out+=printException(e);
	    				StackTraceElement[] st= e.getStackTrace();
	    				out+=printTrace(st);
	    				Throwable cause=e.getCause();
	    				while(cause!=null){
	        				out+=printException(cause);
	        				st= cause.getStackTrace();
	        				out+=printTrace(st);
	    					cause=cause.getCause();
	    				}
	    				out+="--- Trace of caller of unhandled Exception in Async Thread ---\n";
	    				out+=printTrace(trace);
	    				System.err.println(out);
    				}
    				exception=transformException(e);
    				exceptions.add(exception); //push on list of occured exceptions
    			}
				running=false;
    			if(!Thread.currentThread().getName().equals("JavaFX Application Thread")){
    				Throwable ex=transformException(e);
    				throwException(ex);
    			}
    		}
    	}
    	
    	/**
    	 * Throws a transformed exception.
    	 * @param exception the exception to throw
    	 */
    	protected void throwException(Throwable exception){
    		if(exception instanceof RuntimeException)
            	throw((RuntimeException)exception);
            else if(exception instanceof Error)
            	throw((Error)exception);
    	}
    	/**
    	 * Transforms a exception to be throwable. Basically wraps the exception in a RuntimeException,
    	 * if it is not already one.
    	 * @param exception the exception
    	 * @return the throwable exception
    	 */
    	protected Throwable transformException(Throwable exception){
        	if(exception instanceof ExecutionException){ //remove ExecutionException
        		exception=exception.getCause();
        	}
            if(exception instanceof RuntimeException) 
            	return ((RuntimeException)exception);
            if(exception instanceof Error)
            	return ((Error)exception);
            else
            	return new RuntimeException(exception);
        }
    	@Override
    	public X call() throws Exception {
    		run();
    		return get();
    	}
    	
    	
    	
    	@Override
		public X get() throws InterruptedException, ExecutionException {
			try{
				return super.get();
			}catch(Exception e){
				if((!running)&&(exception!=null)){
					exceptions.remove(exception);
					exception=null;
				}
				throw(e);
			}
		}
		@Override
		public X get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			try{
				return super.get(timeout, unit);
			}catch(Exception e){
				if((!running)&&(exception!=null)){
					exceptions.remove(exception);
					exception=null;
				}
				throw(e);
			}
		}
		/**
    	 * Get a string for the Exception header.
    	 * @param e the exception
    	 * @return the line containing the header
    	 */
    	protected String printException(Throwable e){
    		return e.getClass().getName()+": "+e.getMessage()+"\n";
    	}
    	/**
    	 * Prints a stacktrace to a String.
    	 * @param st the stacktrace
    	 * @return the String
    	 */
    	protected String printTrace(StackTraceElement[] st){
    		String ret="";
			for(int i=0;i<st.length;i++){
				ret+="\t"+st[i].toString()+"\n";
			}
			return ret;
    	}
    	
    }
    
    
}
