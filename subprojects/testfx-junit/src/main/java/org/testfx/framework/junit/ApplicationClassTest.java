package org.testfx.framework.junit;

import java.util.Map;

import javafx.application.Application;
import javafx.scene.Node;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxRobot;
import org.testfx.testcase.api.ApplicationClassTestBase;

/**
 * The base class that your JUnit test classes should extend from that interact
 * with and/or verify the state of a JavaFX <b>Application</b>. Such test
 * classes, containing one or more {@code @Test}-annotated methods (individual
 * test cases), can interact with a JavaFX UI using the {@link FxRobot} methods
 * that test class will inherit from {@code FxRobot}. Verifying the state of the
 * UI can be accomplished by using either the
 * <a href="http://hamcrest.org/">Hamcrest</a> based
 * {@link org.testfx.api.FxAssert#verifyThat} or the
 * <a href="http://joel-costigliola.github.io/assertj/">AssertJ</a> based
 * {@link org.testfx.assertions.api.Assertions#assertThat(Node)}.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 * public class TestApplicationTest extends ApplicationClassTest<TestApplication> {
 *
 *     
 *     public String[] getDefaultArgs() {
 *         return new String[] {"DefaultArg"}; //define default arguments (if not null)
 *     }
 *     
 *     {@literal @}Override
 *     public Map<String, String[]> getTestArgsMap() {
 *         HashMap<String,String[]> ret=new HashMap<>();
 *         ret.put("testWithApplicationArguments", new String[] {"OwnArgs"});
 *         ret.put("testWithOutApplicationArguments", null);
 *         return ret;
 *     }
 *
 *     {@literal @}Override
 *     public TestApplication createApplication() {
 *         return new TestApplication();
 *     }
 *
 *     {@literal @}Test
 *     public void enterTextTest() {
 *         // when:
 *         clickOn("#someTextField");
 *         type(KeyCode.A);
 *         type(KeyCode.B);
 *
 *         // then:
 *         assertTrue(lookup(TextMatchers.hasText("AB")).tryQuery().orElse(null)!=null);
 *     }
 *     
 *     {@literal @}Test
 *     public void testWithApplicationArguments() {
 *         // ... test behavior with launch arguments OwnArgs
 *     }
 *     
 *     {@literal @}Test
 *     public void testWithOutApplicationArguments() {
 *         // ... test behavior with no launch arguments
 *     }
 * }
 * }
 * </pre>
 * 
 */
public abstract class ApplicationClassTest<T extends Application> extends ApplicationClassTestBase<T> {

    @Rule
    public TestName testName = new TestName();

    /**
     * Returns the arguments for the application to use in the current test. If the
     * current test method is found in {@link #getTestArgsMap()}, the value is used
     * as arguments. Otherwise, the argumets are set to the return value of
     * {@link #getDefaultArgs()}
     * 
     */
    @Override
    public String[] getArguments() {
        String[] args = null;
        Map<String, String[]> test2args = getTestArgsMap();
        if (test2args != null) {
            if (test2args.containsKey(testName.getMethodName())) {
                args = test2args.get(testName.getMethodName()); // may also be null (intentionally)
            } else {
                args = getDefaultArgs();
            }
        } else {
            args = getDefaultArgs();
        }
        return args;
    }

    /**
     * Returns a map with test method names to the arguments for the application
     * that should be used in the test method with the given name.
     * 
     * @return a map with the test method name as key and the application arguments
     *         as value
     */
    public Map<String, String[]> getTestArgsMap() {
        return null;
    }

    /**
     * Returns the default arguments for the application. The default arguments are
     * used, when no arguments for the current test are found in
     * {{@link #getTestArgsMap()}.
     * 
     * @return the default arguments for the application
     */
    public String[] getDefaultArgs() {
        return null;
    }

    @BeforeClass
    public static void beforeAll() throws Throwable {
        ApplicationClassTestBase.beforeAll();
    }

    @AfterClass
    public static void afterAll() throws Throwable {
        ApplicationClassTestBase.afterAll();
    }

    @Override
    @Before
    public final void beforeTest() throws Throwable {
        super.beforeTest();
    }

    @Override
    @After
    public final void afterTest() throws Throwable {
        super.afterTest();
    }

}
