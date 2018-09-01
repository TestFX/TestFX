/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.testfx.internal.JavaVersionAdapter;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Stores a list of events that have been fired since the start of a test; useful for debugging.
 * Use {@link #beginStoringFiredEventsOf(Stage)} to start storing a stage's fired events and
 * {@link #stopStoringFiredEvents()} when finished and cleaning up this object.
 */
public final class FiredEvents {

    public static FiredEvents beginStoringFiredEventsOf(Stage... stage) {
        return new FiredEvents(stage);
    }
    
    /**
     * Returns a FiredEvents instance, that collects all events of all top level UI-Components.<br>
     * Currently only stages are supported.
     * @return FiredEvents instance, that collects all events of all top level UI-Components
     */
    public static FiredEvents beginStoringFiredEvents() {
        // Should query stages only on Fx-Thread, may throw concurrent modification otherwise?
        Future<Stage[]> future = WaitForAsyncUtils.asyncFx(() -> {
            List<Window> windows = JavaVersionAdapter.getWindows();
            return (Stage[]) windows.stream().filter(w -> w instanceof Stage)
                    .map(w -> (Stage)w).collect(Collectors.toList()).toArray(new Stage[0]);
        });
        try {
            return new FiredEvents(future.get());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to get current stages", e);
        }
    }

    // should also be queried only on Fx-Thread
    private final List<Event> events = new LinkedList<>();
    private final EventHandler<Event> addFiredEvent;
    private final Stage[] stages;

    private FiredEvents(Stage... stage) {
        stages = stage;
        if (stage == null) {
            addFiredEvent = null;
        } else {
            addFiredEvent = events::add;
            WaitForAsyncUtils.asyncFx(() -> {
                for (Stage s : stage) {
                    if (s != null) {
                        s.addEventFilter(EventType.ROOT, addFiredEvent);
                    }
                }
            });
        }
        
    }

    public final List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }
    
    /**
     * Checks if a event that matches the given predicate is in the list of Events
     * @param eventMatcher the predicate each event will be tested for
     * @return true if any element in the list matches the given predicate
     */
    public final boolean hasEvent(Predicate<Event> eventMatcher) {
        return events.stream().anyMatch(eventMatcher);
    }
    /**
     * Checks if the event list matches the given predicate
     * @param eventMatcher the predicate events will be tested for
     * @return true if the matcher is true for the event stream
     */
    public final boolean hasEvents(Predicate<Stream<Event>> eventMatcher) {
        return eventMatcher.test(events.stream());
    }

    public final void clearEvents() {
        events.clear();
    }

    public final void stopStoringFiredEvents() {
        if (addFiredEvent != null) {
            WaitForAsyncUtils.asyncFx(() -> {
                for (Stage s : stages) {
                    if (s != null) {
                        s.removeEventFilter(EventType.ROOT, addFiredEvent);
                    }
                }
            });
        }
    }

}
