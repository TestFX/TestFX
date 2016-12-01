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
package org.loadui.testfx;

import java.util.function.Predicate;
import javafx.scene.Node;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.loadui.testfx.GuiTest.find;

public class Assertions {
    @SuppressWarnings("unchecked")
    public static void assertNodeExists(Matcher<?> matcher) {
        find((Matcher<Object>) matcher);
    }

    public static void assertNodeExists(String query) {
        find(query);
    }

    public static <T> void verifyThat(T value, Matcher<? super T> matcher) {
        verifyThat("", value, matcher);
    }

    public static <T> void verifyThat(String reason, T value, Matcher<? super T> matcher) {
        try {
            assertThat(reason, value, matcher);
        }
        catch (AssertionError e) {
            throw new AssertionError(e.getMessage() + " Screenshot saved as " +
                GuiTest.captureScreenshot().getAbsolutePath(), e);
        }
    }

    public static <T extends Node> void verifyThat(String query, Predicate<T> predicate) {
        T node = find(query);
        if (!predicate.test(node)) {
            throw new AssertionError("Predicate failed for query '" + query +
                "'. Screenshot saved as " + GuiTest.captureScreenshot().getAbsolutePath());
        }
    }

    public static <T extends Node> void verifyThat(T node, Predicate<T> predicate) {
        if (!predicate.test(node)) {
            throw new AssertionError("Predicate failed for '" + node + "'. Screenshot saved as " +
                GuiTest.captureScreenshot().getAbsolutePath());
        }
    }
}
