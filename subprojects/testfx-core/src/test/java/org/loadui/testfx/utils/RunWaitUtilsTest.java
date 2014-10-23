/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.utils;

import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;

public class RunWaitUtilsTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test(timeout=1000)
    public void callOutside() throws Exception {
        // when:
        Future<String> future = RunWaitUtils.callOutside(() -> "foo");

        // then:
        RunWaitUtils.sleep(10, MILLISECONDS);
        assertThat(future.get(), Matchers.is("foo"));
    }

    @Test(timeout=1000)
    public void callOutside_with_sleep() throws Exception {
        // when:
        Future<String> future = RunWaitUtils.callOutside(() -> {
            RunWaitUtils.sleep(50, MILLISECONDS);
            return "foo";
        });

        // then:
        assertThat(future.isDone(), Matchers.is(false));
        RunWaitUtils.sleep(100, MILLISECONDS);
        assertThat(future.get(), Matchers.is("foo"));
    }

    @Test(timeout=1000)
    public void waitFor_with_future() throws Exception {
        // given:
        Future<Void> future = RunWaitUtils.callOutside(() -> null);

        // expect:
        RunWaitUtils.waitFor(50, MILLISECONDS, future);
    }

    @Test(timeout=1000)
    public void waitFor_with_future_with_sleep() throws Exception {
        // given:
        Future<Void> future = RunWaitUtils.runOutside(() -> {
            RunWaitUtils.sleep(100, MILLISECONDS);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        RunWaitUtils.waitFor(50, MILLISECONDS, future);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable() throws Exception {
        // expect:
        RunWaitUtils.waitFor(250, MILLISECONDS, () -> true);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_sleep() throws Exception {
        // expect:
        RunWaitUtils.waitFor(250, MILLISECONDS, () -> {
            RunWaitUtils.sleep(50, MILLISECONDS);
            return true;
        });
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_false() throws Exception {
        // expect:
        thrown.expect(TimeoutException.class);
        RunWaitUtils.waitFor(250, MILLISECONDS, () -> false);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        RunWaitUtils.runOutside(() -> {
            RunWaitUtils.sleep(50, MILLISECONDS);
            property.set(true);
        });

        // expect:
        RunWaitUtils.waitFor(250, MILLISECONDS, property);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue_with_false() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        RunWaitUtils.runOutside(() -> {
            RunWaitUtils.sleep(50, MILLISECONDS);
            property.set(false);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        RunWaitUtils.waitFor(250, MILLISECONDS, property);
    }

}
