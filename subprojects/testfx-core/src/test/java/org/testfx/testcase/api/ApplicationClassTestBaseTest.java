package org.testfx.testcase.api;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.testcase.api.support.TestApplication;
import org.testfx.testcase.api.support.TestApplication.AppState;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationClassTestBaseTest {

    // lifecycle sequence is tested with each test in MyTestApp

    @Test
    public void a1_dirtyTest() throws Throwable {
        ApplicationClassTestBaseSimulator.args = null;
        // given
        ApplicationClassTestBaseSimulator test = new ApplicationClassTestBaseSimulator();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        // when
        test.moveTo("#myTestAppBtn"); // prevent clicking in the background...
        test.press(KeyCode.SHORTCUT);
        test.press(MouseButton.PRIMARY);

        test.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void a2_checkBasicCleanup() throws Throwable {
        ApplicationClassTestBaseSimulator.args = null;
        ApplicationClassTestBaseSimulator testAfter = new ApplicationClassTestBaseSimulator();
        testAfter.beforeTest();
        testAfter.moveTo("#myTestAppBtn");
        testAfter.clickOn(MouseButton.SECONDARY);
        // no modifier down
        assertFalse("Keys were not cleared", testAfter.getApplication().btnEvent.isShortcutDown());
        // primary not down
        assertFalse("Buttons were not cleared", testAfter.getApplication().btnEvent.isPrimaryButtonDown());

        testAfter.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void noArgsTest() throws Throwable {
        // given
        ApplicationClassTestBaseSimulator.args = null;
        ApplicationClassTestBaseSimulator test = new ApplicationClassTestBaseSimulator();
        ApplicationClassTestBase.beforeAll();
        test.beforeTest();

        // then
        assertThat("Application state invalid", test.getApplication().state, equalTo(AppState.STARTED));
        assertThat(test.lookup("#myTestAppBtn").tryQuery().orElse(null), notNullValue());
        assertThat(test.lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue("Test of MyAppTF not correctly initialized",
                test.lookup(TextMatchers.hasText("InitialText")).tryQuery().orElse(null) != null);

        // when
        test.clickOn("#myTestAppBtn");

        // then
        assertTrue("Node with clickedText not found",
                test.lookup(TextMatchers.hasText("clickedText")).tryQuery().orElse(null) != null);

        // when
        test.afterTest();
        ApplicationClassTestBase.afterAll();

        // then
        assertThat("Application state invalid", test.getApplication().state, equalTo(AppState.STOPPED));

    }

    @Test
    public void argsTest() throws Throwable {
        // given
        ApplicationClassTestBaseSimulator.args = new String[] {"OwnText"}; // when
        ApplicationClassTestBaseSimulator test = new ApplicationClassTestBaseSimulator();
        ApplicationClassTestBase.beforeAll();
        test.beforeTest();

        // then
        assertThat(test.lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue("Test of MyAppTF not correctly initialized",
                test.lookup(TextMatchers.hasText("OwnText")).tryQuery().orElse(null) != null);

        test.afterTest();
        ApplicationClassTestBase.afterAll();
    }

    @Test
    public void stageTest() throws Throwable {
        // given
        ApplicationClassTestBaseSimulator test = new ApplicationClassTestBaseSimulator();
        ApplicationClassTestBase.beforeAll();
        test.beforeTest();
        assertThat(test.lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());

        // then
        Node n = test.lookup("#myTestAppTF").query();
        Window s = n.getScene().getWindow();
        assertThat("Returned stage not correct", test.getTestStage(), equalTo(s));

        test.afterTest();
        ApplicationClassTestBase.afterAll();
    }

    private static class ApplicationClassTestBaseSimulator extends ApplicationClassTestBase<TestApplication> {
        public static String[] args;

        @Override
        public TestApplication createApplication() {
            return new TestApplication();
        }

        @Override
        public String[] getArguments() {
            return args;
        }

    }

}
