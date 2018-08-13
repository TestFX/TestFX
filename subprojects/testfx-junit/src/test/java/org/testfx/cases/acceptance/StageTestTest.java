package org.testfx.cases.acceptance;

import javafx.scene.input.MouseButton;

import org.junit.Test;
import org.testfx.cases.acceptance.classes.TestStage;
import org.testfx.framework.junit.StageTest;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class StageTestTest extends StageTest<TestStage> {

    @Test
    public void asParentTest() throws Throwable {
        assertThat("Node doesn't exist after opening", lookup("#button").query(), notNullValue());
        assertThat(getTestStage().getTestBox().btnEvent, nullValue());
        moveTo("#button");
        clickOn(MouseButton.PRIMARY);
        assertThat(getTestStage().getTestBox().btnEvent, notNullValue());
    }

    @Override
    public TestStage createStage() {
        return new TestStage();
    }
}
