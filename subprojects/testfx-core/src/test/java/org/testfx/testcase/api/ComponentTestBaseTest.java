package org.testfx.testcase.api;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.testcase.api.support.TestComponent;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComponentTestBaseTest {

    @Test
    public void a1_dirtyTest() throws Throwable {
        // given
        ComponentTestParent test = new ComponentTestParent();
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
        ComponentTestParent testAfter = new ComponentTestParent();
        testAfter.beforeTest();
        testAfter.moveTo("#button");
        testAfter.clickOn(MouseButton.SECONDARY);
        // no modifier down
        assertFalse("Keys were not cleared", testAfter.getComponent().btnEvent.isShortcutDown());
        // primary not down
        assertFalse("Buttons were not cleared", testAfter.getComponent().btnEvent.isPrimaryButtonDown());

        testAfter.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void asNodeTest() throws Throwable {
        ComponentTestNode test = new ComponentTestNode();
        ComponentTestBase.beforeAll();
        test.beforeTest();
        assertThat("Node doesn't exist after opening", test.lookup("#dut").query(), notNullValue());
        test.afterTest();
        ComponentTestBase.afterAll();
        assertThat("Node still exists after closing", test.lookup("#dut").tryQuery().orElse(null), nullValue());
    }

    @Test
    public void asParentTest() throws Throwable {
        ComponentTestParent test = new ComponentTestParent();
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
        ComponentTestNode test = new ComponentTestNode();
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
        test = new ComponentTestNode();
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
        ComponentTestParent test = new ComponentTestParent();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        Node n = test.lookup("#button").query();
        Window s = n.getScene().getWindow();
        assertThat("Returned stage not correct", test.getTestStage(), equalTo(s));

        test.afterTest();
        ComponentTestBase.afterAll();
    }

    /////////// required to test cleanup of TestCaseBase here, see TestCaseBaseTest
    /////////// /////////////
    @Test
    public void cleanUpBefore() throws Throwable {
        // given
        ComponentTestParent testBefore = new ComponentTestParent();
        ComponentTestBase.beforeAll();
        testBefore.beforeTest();

        // when
        testBefore.moveTo("#button"); // prevent clicking in the background...
        testBefore.press(KeyCode.SHORTCUT);
        testBefore.press(MouseButton.PRIMARY);
        // omitting cleanup of test before

        // then
        ComponentTestParent test = new ComponentTestParent();
        ComponentTestBase.beforeAll();
        test.beforeTest();
        test.moveTo("#button");
        test.clickOn(MouseButton.SECONDARY);
        // no modifier down
        assertFalse("Keys were not cleared", test.getComponent().btnEvent.isShortcutDown());
        assertFalse("Buttons were not cleared", test.getComponent().btnEvent.isPrimaryButtonDown());

        test.afterTest();
        ComponentTestBase.afterAll();
    }

    @Test
    public void cleanUpAfter() throws Throwable {
        // given
        ComponentTestParent test = new ComponentTestParent();
        ComponentTestBase.beforeAll();
        test.beforeTest();

        // when
        test.moveTo("#button"); // prevent clicking in the background...
        test.press(KeyCode.SHORTCUT);
        test.press(MouseButton.PRIMARY);
        test.afterTest();
        ComponentTestBase.afterAll();

        // then
        ComponentTestParentNoCleanUPBefore testAfter = new ComponentTestParentNoCleanUPBefore();
        testAfter.beforeTest();
        testAfter.moveTo("#button");
        testAfter.clickOn(MouseButton.SECONDARY);
        // no modifier down
        assertFalse("Keys were not cleared", testAfter.getComponent().btnEvent.isShortcutDown());
        assertFalse("Buttons were not cleared", testAfter.getComponent().btnEvent.isPrimaryButtonDown());

        testAfter.afterTest();
        ComponentTestBase.afterAll();
    }

    /////////// TestCases /////////////

    // Node
    private class ComponentTestNode extends ComponentTestBase<Rectangle> {

        @Override
        public Rectangle createComponent() {
            Rectangle rect = new Rectangle(200, 200, 200, 100);
            rect.setId("dut");
            return rect;
        }

    }

    // Parent
    private class ComponentTestParent extends ComponentTestBase<TestComponent> {

        @Override
        public TestComponent createComponent() {
            return new TestComponent();
        }

    }

    private class ComponentTestParentNoCleanUPBefore extends ComponentTestBase<TestComponent> {

        @Override
        public void beforeTest() throws Throwable {
            FxToolkit.registerStage(() -> {
                testStage = new Stage();
                testStage.centerOnScreen();
                testStage.initStyle(StageStyle.UNDECORATED);
                component = createComponent();
                Parent content = null;
                if (component instanceof Parent) {
                    content = (Parent) component;
                } else {
                    content = new Pane();
                    if (component != null) {
                        ((Pane) content).getChildren().add(component);
                    }
                }

                testScene = new Scene(content);
                testStage.setScene(testScene);
                testStage.show();
                return testStage;
            });
        }

        @Override
        public TestComponent createComponent() {
            return new TestComponent();
        }

    }
}
