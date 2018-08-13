package org.testfx.cases;

import javafx.scene.Node;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.testfx.testcase.api.ComponentTestBase;

/**
 * Internal JUnit4 adapter of ComponentTestBase. The internal adapter prevents
 * circular dependencies between projects. It also adds the TestFXRule and a
 * timeout of 10 seconds to each test.
 *
 * @param <T> The type of the node to test
 */
public abstract class InternalJUnitTest<T extends Node> extends ComponentTestBase<T> {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(Timeout.millis(10000));

    @BeforeClass
    public static void beforeClass() throws Throwable {
        ComponentTestBase.beforeAll();
    }

    @AfterClass
    public static void afterClass() throws Throwable {
        ComponentTestBase.afterAll();
    }

    @Before
    @Override
    public final void beforeTest() throws Throwable {
        super.beforeTest();
    }

    @After
    @Override
    public final void afterTest() throws Throwable {
        super.afterTest();
    }

}
