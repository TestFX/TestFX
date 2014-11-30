package org.testfx.matcher.base;

import javafx.scene.Parent;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class ParentMatchers {

    //---------------------------------------------------------------------------------------------
    // MATCHER METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Parent> hasChild(String nodeQuery) {
        return null;
    }

    @Factory
    public static Matcher<Parent> hasChildren(int amount, String nodeQuery) {
        return null;
    }

}
