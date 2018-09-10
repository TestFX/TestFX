package org.testfx.service.support.impl;

import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.service.support.FiredEvents;

import static org.junit.Assert.assertTrue;

public class FiredEventsTest extends InternalTestCaseBase {

    @Test
    public void hasEventAllStagesTest() {
        // given
        moveTo(getTestStage());
        FiredEvents events = FiredEvents.beginStoringFiredEvents();
        assertTrue("MouseEvent already in list of events", !events.hasEvent(e -> e instanceof MouseEvent));

        // when
        clickOn(MouseButton.PRIMARY);

        // then
        assertTrue("No MouseEvent in list of events", events.hasEvent(e -> e instanceof MouseEvent));
    }

    @Test
    public void hasEventTest() {
        // given
        moveTo(getTestStage());
        FiredEvents events = FiredEvents.beginStoringFiredEventsOf(getTestStage());
        assertTrue("MouseEvent already in list of events", !events.hasEvent(e -> e instanceof MouseEvent));

        // when
        clickOn(MouseButton.PRIMARY);

        // then
        assertTrue("No MouseEvent in list of events", events.hasEvent(e -> e instanceof MouseEvent));
    }

    @Test
    public void hasEventsTest() {
        // given
        moveTo(getTestStage());
        FiredEvents events = FiredEvents.beginStoringFiredEventsOf(getTestStage());
        Predicate<Stream<Event>> matcher = s -> s.filter(e -> e instanceof MouseEvent).count() == 3;
        assertTrue("MouseEvent already in list of events", !events.hasEvents(matcher));

        // when triggers (press, release and click)
        clickOn(MouseButton.PRIMARY);

        // then
        assertTrue("No MouseEvent in list of events", events.hasEvents(matcher));
    }
}
