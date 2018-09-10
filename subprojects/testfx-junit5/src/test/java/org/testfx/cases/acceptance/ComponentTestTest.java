package org.testfx.cases.acceptance;

import javafx.scene.input.MouseButton;

import org.junit.jupiter.api.Test;
import org.testfx.cases.acceptance.classes.TestComponent;
import org.testfx.framework.junit5.ComponentTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class ComponentTestTest extends ComponentTest<TestComponent> {

    // functionality has already been checked in core, just simple verification
    @Test
    public void asParentTest() throws Throwable {
        assertThat("Node doesn't exist after opening", lookup("#button").query(), notNullValue());
        assertThat(getComponent().btnEvent, nullValue());
        moveTo("#button");
        clickOn(MouseButton.PRIMARY);
        assertThat(getComponent().btnEvent, notNullValue());
    }

    @Override
    public TestComponent createComponent() {
        return new TestComponent();
    }
}
