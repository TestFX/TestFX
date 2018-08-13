package org.testfx.cases.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.testfx.cases.acceptance.classes.TestApplication;
import org.testfx.cases.acceptance.classes.TestApplication.AppState;
import org.testfx.framework.junit.ApplicationClassTest;
import org.testfx.matcher.control.TextMatchers;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ApplicationClassTestTest extends ApplicationClassTest<TestApplication> {

    @Override
    public Map<String, String[]> getTestArgsMap() {
        HashMap<String, String[]> ret = new HashMap<>();
        ret.put("argsTest", new String[] {"OwnText"});
        ret.put("argNullTest", null);
        return ret;
    }

    @Override
    public String[] getDefaultArgs() {
        return new String[] {"DefaultText"};
    }

    @Override
    public TestApplication createApplication() {
        return new TestApplication();
    }

    @Test
    public void defaultArgsTest() throws Throwable {
        // given

        // then
        assertThat("Application state invalid", getApplication().state, equalTo(AppState.STARTED));
        assertThat(lookup("#myTestAppBtn").tryQuery().orElse(null), notNullValue());
        assertThat(lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue("Test of MyAppTF not correctly initialized",
                lookup(TextMatchers.hasText("DefaultText")).tryQuery().orElse(null) != null);

        // when
        clickOn("#myTestAppBtn");

        // then
        assertTrue("Node with clickedText not found",
                lookup(TextMatchers.hasText("clickedText")).tryQuery().orElse(null) != null);

    }

    @Test
    public void argsTest() throws Throwable {
        // given
        // arguments set in class initialization

        // then
        assertThat(lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue("Test of MyAppTF not correctly initialized",
                lookup(TextMatchers.hasText("OwnText")).tryQuery().orElse(null) != null);
    }

    @Test
    public void argNullTest() throws Throwable {
        // given
        // arguments set in class initialization

        // then
        assertThat(lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue("Test of MyAppTF not correctly initialized",
                lookup(TextMatchers.hasText("InitialText")).tryQuery().orElse(null) != null);
    }

}
