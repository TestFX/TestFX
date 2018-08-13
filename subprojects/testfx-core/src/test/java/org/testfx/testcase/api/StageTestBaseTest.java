package org.testfx.testcase.api;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.testcase.api.support.TestStage;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StageTestBaseTest {

    @Test
    public void a1_dirtyTest() throws Throwable {
        // given
        StageTest test = new StageTest();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        // when
        test.moveTo("#button"); // prevent clicking in the background...
        test.press(KeyCode.SHORTCUT);
        test.press(MouseButton.PRIMARY);

        test.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void a2_checkBasicCleanup() throws Throwable {
        StageTest testAfter = new StageTest();
        testAfter.beforeTest();
        testAfter.moveTo("#button");
        testAfter.clickOn(MouseButton.SECONDARY);
        // no modifier down
        assertFalse("Keys were not cleared", testAfter.getTestStage().getTestBox().btnEvent.isShortcutDown());
        // primary not down
        assertFalse("Buttons were not cleared", testAfter.getTestStage().getTestBox().btnEvent.isPrimaryButtonDown());

        testAfter.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void asParentTest() throws Throwable {
        StageTest test = new StageTest();
        ComponentTestBase.beforeAll();
        test.beforeTest();
        assertThat("Node doesn't exist after opening", test.lookup("#button").query(), notNullValue());
        test.afterTest();
        ComponentTestBase.afterAll();
        assertThat("Node still exists after closing", test.lookup("#button").tryQuery().orElse(null), nullValue());
    }

    // Tests should start at a defined position, regardless of the position of the
    // test before
    @Test
    public void isolationTest() throws Throwable {
        // given
        StageTest test = new StageTest();
        ComponentTestBase.beforeAll();
        test.beforeTest();
        final double x = test.getTestStage().getX();
        final double y = test.getTestStage().getY();

        // when
        test.getTestStage().setX(0);
        test.getTestStage().setY(0);
        test.sleep(200);
        test.afterTest();
        ComponentTestBase.afterAll();
        test = new StageTest();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        // then
        assertThat("X of test changed", test.getTestStage().getX(), equalTo(x));
        assertThat("Y of test changed", test.getTestStage().getY(), equalTo(y));
        test.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void stageTest() throws Throwable {
        StageTest test = new StageTest();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        Node n = test.lookup("#button").query();
        Window s = n.getScene().getWindow();
        assertThat("Returned stage not correct", test.getTestStage(), equalTo(s));

        test.afterTest();
        ComponentTestBase.afterAll();
    }

    private class StageTest extends StageTestBase<TestStage> {

        @Override
        public TestStage createStage() {
            return new TestStage();
        }

    }

}
