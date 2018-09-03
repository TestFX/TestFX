package org.testfx.util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.event.Event;

import org.testfx.service.support.FiredEvents;

/**
 * This class allows waiting for events to arrive at the JavaFx Thread. When the class is created, it will
 * start storing a list of all events that occurred. The method waitFor will wait until the specified condition
 * is fulfilled (or a timeout occurs).<br>
 * Usage:<br>
 * <ul>
 * <li>Create a instance using the static factory methods {@link #ofEvent(long, Predicate, boolean)} or 
 * {@link #ofStream(long, Predicate, boolean)}</li>
 * <li>Run the code that triggers the event</li>
 * <li>Call {@link #waitFor()} to wait for the event to happen</li>
 * </ul>
 */
public class WaitForInputEvent extends WaitForAsyncUtils.ConditionWaiter {

    FiredEvents events;
    final Function<FiredEvents, Boolean> matcher;
    final boolean throwTimeout;

    /**
     * Creates a instance that waits until the stream of all collected events matches the given predicate.
     * May be used for example to count the number of events of a specific type etc.. 
     * @param timeoutMS the timeout in milliseconds
     * @param matcher the matcher to wait for
     * @param throwTimeout if true, a exception will be thrown if a timeout occurred
     * @return the instance of the waiter
     */
    public static WaitForInputEvent ofStream(long timeoutMS, Predicate<Stream<Event>> matcher, boolean throwTimeout) {
        return new WaitForInputEvent(timeoutMS, f -> f.hasEvents(matcher), throwTimeout);
    }
    /**
     * Creates a instance that waits until at least one event occurs, that matches the given predicate.
     * @param timeoutMS the timeout in milliseconds
     * @param matcher
     * @param throwTimeout if true, a exception will be thrown if a timeout occurred
     * @return the instance of the waiter
     */
    public static WaitForInputEvent ofEvent(long timeoutMS, Predicate<Event> matcher, boolean throwTimeout) {
        return new WaitForInputEvent(timeoutMS, f -> f.hasEvent(matcher), throwTimeout);
    }
    /**
     * Creates a instance that waits until the list of all collected events matches the given predicate.
     * May be used for example to count the number of events of a specific type etc.. 
     * @param timeoutMS the timeout in milliseconds
     * @param matcher the matcher to wait for
     * @param throwTimeout if true, a exception will be thrown if a timeout occurred
     * @return the instance of the waiter
     */
    public static WaitForInputEvent ofList(long timeoutMS, Predicate<List<Event>> matcher, boolean throwTimeout) {
        return new WaitForInputEvent(timeoutMS, f -> matcher.test(f.getEvents()), throwTimeout);
    }

    /**
     * Will construct the waiter and immediately start collecting elements. It will
     * wait until either a event matches the given matcher or the defined timeout is
     * reached. The timeout will start when {@link #waitFor()} is called.
     * 
     * @param timeoutMS    the timeout for waiting for events
     * @param matcher      the matcher
     * @param throwTimeout if true a exception is thrown in case of a timeout
     */
    protected WaitForInputEvent(long timeoutMS, Function<FiredEvents, Boolean> matcher, boolean throwTimeout) {
        super(timeoutMS, 0);
        this.matcher = matcher;
        this.throwTimeout = throwTimeout;
        events = FiredEvents.beginStoringFiredEvents();
    }

    @Override
    protected boolean checkCondition() {
        if (events == null || matcher == null) {
            return true;
        }
        return matcher.apply(events);
    }

    @Override
    public void waitFor() {
        try {
            super.waitFor();
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }

    @Override
    protected void onTimeout() {
        if (throwTimeout) {
            throw new TestFxTimeoutException("Waiting for InputEvents timed out");
        }
    }
    @Override
    protected void onInterrupted() {
        System.err.println("Interrupted during wait, timing may be incorrect");
    }

}
