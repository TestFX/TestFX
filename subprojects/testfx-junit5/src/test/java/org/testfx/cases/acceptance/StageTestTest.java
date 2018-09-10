package org.testfx.cases.acceptance;

import javafx.scene.input.MouseButton;

import org.junit.jupiter.api.Test;
import org.testfx.cases.acceptance.classes.TestStage;
import org.testfx.framework.junit5.StageTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

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
