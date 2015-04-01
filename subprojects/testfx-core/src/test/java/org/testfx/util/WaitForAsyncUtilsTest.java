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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import static org.hamcrest.Matchers.instanceOf;

public class WaitForAsyncUtilsTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test(timeout=1000)
    public void async_callable() throws Exception {
        // when:
        Future<String> future = WaitForAsyncUtils.async(() -> "foo");

        // then:
        WaitForAsyncUtils.sleep(10, MILLISECONDS);
        assertThat(future.get(), Matchers.is("foo"));
    }

    @Test(timeout=1000)
    public void async_callable_with_sleep() throws Exception {
        // when:
        Future<String> future = WaitForAsyncUtils.async(() -> {
            WaitForAsyncUtils.sleep(50, MILLISECONDS);
            return "foo";
        });

        // then:
        assertThat(future.isDone(), Matchers.is(false));
        WaitForAsyncUtils.sleep(100, MILLISECONDS);
        assertThat(future.get(), Matchers.is("foo"));
    }

    @Test(timeout=1000)
    public void async_callable_with_exception() throws Exception {
        // given:
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        Future<Void> future = WaitForAsyncUtils.async(callable);

        // expect:
        thrown.expectCause(instanceOf(ExecutionException.class));
        WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
    }

    @Test(timeout=1000)
    public void waitFor_with_future() throws Exception {
        // given:
        Future<Void> future = WaitForAsyncUtils.async(() -> null);

        // expect:
        WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
    }

    @Test(timeout=1000)
    public void waitFor_with_future_with_sleep() throws Exception {
        // given:
        Future<Void> future = WaitForAsyncUtils.async(() -> {
            WaitForAsyncUtils.sleep(100, MILLISECONDS);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable() throws Exception {
        // expect:
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> true);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_sleep() throws Exception {
        // expect:
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> {
            WaitForAsyncUtils.sleep(50, MILLISECONDS);
            return true;
        });
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_false() throws Exception {
        // expect:
        thrown.expect(TimeoutException.class);
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> false);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanCallable_with_exception() throws Exception {
        // expect:
        thrown.expectCause(instanceOf(UnsupportedOperationException.class));
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> {
            throw new UnsupportedOperationException();
        });
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        WaitForAsyncUtils.async(() -> {
            WaitForAsyncUtils.sleep(50, MILLISECONDS);
            property.set(true);
        });

        // expect:
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, property);
    }

    @Test(timeout=1000)
    public void waitFor_with_booleanValue_with_false() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);
        WaitForAsyncUtils.async(() -> {
            WaitForAsyncUtils.sleep(50, MILLISECONDS);
            property.set(false);
        });

        // expect:
        thrown.expect(TimeoutException.class);
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, property);
    }

}
