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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;

/**
 * Stores a list of events that have been fired since the start of a test; useful for debugging.
 * Use {@link #beginStoringFiredEventsOf(Stage)} to start storing a stage's fired events and
 * {@link #stopStoringFiredEvents()} when finished and cleaning up this object.
 */
public final class FiredEvents {

    public static FiredEvents beginStoringFiredEventsOf(Stage stage) {
        return new FiredEvents(stage);
    }

    private final List<Event> events = new LinkedList<>();
    private final Runnable removeListener;

    private FiredEvents(Stage stage) {
        if (stage == null) {
            removeListener = null;
        } else {
            EventHandler<Event> addFiredEvent = events::add;
            stage.addEventFilter(EventType.ROOT, addFiredEvent);
            removeListener = () -> stage.removeEventFilter(EventType.ROOT, addFiredEvent);
        }
    }

    public final List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public final void clearEvents() {
        events.clear();
    }

    public final void stopStoringFiredEvents() {
        if (removeListener != null) {
            removeListener.run();
        }
    }

}
