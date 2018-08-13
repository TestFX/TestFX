package org.testfx.cases.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.testfx.cases.acceptance.classes.TestApplication;
import org.testfx.cases.acceptance.classes.TestApplication.AppState;
import org.testfx.framework.junit5.ApplicationClassTest;
import org.testfx.matcher.control.TextMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(lookup(TextMatchers.hasText("DefaultText")).tryQuery().orElse(null) != null,
                "Test of MyAppTF not correctly initialized");

        // when
        clickOn("#myTestAppBtn");

        // then
        assertTrue(lookup(TextMatchers.hasText("clickedText")).tryQuery().orElse(null) != null,
                "Node with clickedText not found");

    }

    @Test
    public void argsTest() throws Throwable {
        // given
        // arguments set in class initialization

        // then
        assertThat(lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue(lookup(TextMatchers.hasText("OwnText")).tryQuery().orElse(null) != null,
                "Test of MyAppTF not correctly initialized");
    }

    @Test
    public void argNullTest() throws Throwable {
        // given
        // arguments set in class initialization

        // then
        assertThat(lookup("#myTestAppTF").tryQuery().orElse(null), notNullValue());
        assertTrue(lookup(TextMatchers.hasText("InitialText")).tryQuery().orElse(null) != null,
                "Test of MyAppTF not correctly initialized");
    }

}
