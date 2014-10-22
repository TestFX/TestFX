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

public class InvokeWaitUtilsTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test(timeout=1000)
    public void invokeInThread() {
        // when:
        Future future = InvokeWaitUtils.invokeInThread(() -> {
        });

        // then:
        InvokeWaitUtils.sleep(10, MILLISECONDS);
        assertThat(future.isDone(), Matchers.is(true));
    }

    @Test(timeout=1000)
    public void invokeInThread_with_sleep() {
        // when:
        Future future = InvokeWaitUtils.invokeInThread(() -> {
            InvokeWaitUtils.sleep(50, MILLISECONDS);
        });

        // then:
        assertThat(future.isDone(), Matchers.is(false));
        InvokeWaitUtils.sleep(100, MILLISECONDS);
        assertThat(future.isDone(), Matchers.is(true));
    }

    @Test(timeout=1000)
    public void waitFor_with_future() throws Exception {
        // given:
        Future future = InvokeWaitUtils.invokeInThread(() -> {
        });

        // expect:
        InvokeWaitUtils.waitFor(future, 50, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_future_with_sleep() throws Exception {
        // given:
        Future future = InvokeWaitUtils.invokeInThread(() -> {
            InvokeWaitUtils.sleep(100, MILLISECONDS);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        InvokeWaitUtils.waitFor(future, 50, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable() throws Exception {
        // expect:
        InvokeWaitUtils.waitFor(() -> true, 250, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_sleep() throws Exception {
        // expect:
        InvokeWaitUtils.waitFor(() -> {
            InvokeWaitUtils.sleep(50, MILLISECONDS);
            return true;
        }, 250, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_false() throws Exception {
        // expect:
        thrown.expect(TimeoutException.class);
        InvokeWaitUtils.waitFor(() -> false, 250, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        InvokeWaitUtils.invokeInThread(() -> {
            InvokeWaitUtils.sleep(50, MILLISECONDS);
            property.set(true);
        });

        // expect:
        InvokeWaitUtils.waitFor(property, 250, MILLISECONDS);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue_with_false() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        InvokeWaitUtils.invokeInThread(() -> {
            InvokeWaitUtils.sleep(50, MILLISECONDS);
            property.set(false);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        InvokeWaitUtils.waitFor(property, 250, MILLISECONDS);
    }

}
