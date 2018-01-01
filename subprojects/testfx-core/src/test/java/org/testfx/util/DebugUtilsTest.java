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
package org.testfx.util;

import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.support.FiredEvents;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.DebugUtils.compose;
import static org.testfx.util.DebugUtils.insertHeader;
import static org.testfx.util.DebugUtils.runCode;
import static org.testfx.util.DebugUtils.showFiredEvents;
import static org.testfx.util.DebugUtils.showKeysPressedAtTestFailure;
import static org.testfx.util.DebugUtils.showMouseButtonsPressedAtTestFailure;

public class DebugUtilsTest extends TestCaseBase {

    private static final String INDENT = " ";

    private static final MouseButton MOUSE_BUTTON = MouseButton.PRIMARY;
    private static final KeyCode KEY = KeyCode.A;

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @After
    public void cleanup() {
        release(KEY);
        release(MOUSE_BUTTON);
    }

    @Test
    public void composingMessageWithNoArgs() {
        // when:
        AssertionError error = getThrownErrorPostMapper(compose());

        // then:
        assertThat(error.getMessage(), equalTo(""));
    }

    @Test
    public void composingMessageWithOneArg() {
        // given:
        String text = "text";

        // when:
        AssertionError error = getThrownErrorPostMapper(compose(sb -> sb.append(text)));

        // then:
        assertThat(error.getMessage(), equalTo(text));
    }

    @Test
    public void composingMessageWithMultipleArgs() {
        // given:
        String text = "text";
        String other = "other";

        // when:
        AssertionError error = getThrownErrorPostMapper(compose(sb -> sb.append(text), sb -> sb.append(other)));

        // then:
        assertThat(error.getMessage(), equalTo(text + other));
    }

    @Test
    public void runCodeBlock() {
        // given:
        SimpleBooleanProperty prop = new SimpleBooleanProperty(false);

        // when:
        getThrownErrorPostMapper(runCode(() -> prop.set(true)));

        // then:
        assertThat(prop.get(), is(true));
    }

    @Test
    public void insertHeaderIntoErrorMessage() {
        // given:
        String header = "header";

        // when:
        AssertionError error = getThrownErrorPostMapper(insertHeader(header, INDENT));

        // then:
        assertThat(error.getMessage(), containsString("\n\n" + INDENT + header));
    }

    @Test
    public void showPressedMouseButtonsInErrorMessage() {
        // given:
        Stage stage = FxToolkit.toolkitContext().getRegisteredStage();
        moveTo(stage).moveBy(10, 10).press(MOUSE_BUTTON);

        // when:
        AssertionError error = getThrownErrorPostMapper(showMouseButtonsPressedAtTestFailure(this, INDENT));

        // then:
        assertThat(error.getMessage(), containsString(MOUSE_BUTTON.name()));
    }

    @Test
    public void showPressedKeysInErrorMessage() {
        // given:
        Stage stage = FxToolkit.toolkitContext().getRegisteredStage();
        moveTo(stage).moveBy(10, 10).press(KEY);

        // when:
        AssertionError error = getThrownErrorPostMapper(showKeysPressedAtTestFailure(this, INDENT));

        // then:
        assertThat(error.getMessage(), containsString(KEY.name()));
    }

    @Test
    public void showFiredEventsInErrorMessage() throws TimeoutException {
        // given:
        Stage stage = FxToolkit.setupStage(stage0 -> stage0.setScene(new Scene(new Region(), 300, 300)));
        interact(stage::show);
        moveTo(stage).moveBy(20, 20);

        // and:
        FiredEvents events = FiredEvents.beginStoringFiredEventsOf(stage);
        moveBy(1, 0);
        events.stopStoringFiredEvents();

        // when:
        AssertionError error = getThrownErrorPostMapper(showFiredEvents(events, INDENT));

        // then:
        assertThat(error.getMessage(), containsString("MouseEvent"));
        assertThat(error.getMessage(), not(containsString("ScrollEvent")));
    }

    private AssertionError getThrownErrorPostMapper(Function<StringBuilder, StringBuilder> errorMessageMapper) {
        try {
            // by returning a new StringBuilder, the resulting error message will only have
            // the text we add to it from this point forward.
            verifyThat(false, alwaysFail(), errorMessageMapper.compose(sb -> new StringBuilder()));
            throw new AssertionError("Error: verifyThat did not throw an error when it should have.");
        }
        catch (AssertionError error) {
            return error;
        }
    }

    private Matcher<Object> alwaysFail() {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return false;
            }

            @Override
            public void describeTo(Description description) {}
        };
    }

}
