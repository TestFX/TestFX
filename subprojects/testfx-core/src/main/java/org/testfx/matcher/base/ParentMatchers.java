package org.testfx.matcher.base;

import javafx.scene.Node;
import javafx.scene.Parent;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;

public class ParentMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Parent> hasChild(String nodeQuery) {
        Node node = FxAssert.assertContext().getNodeFinder().node(nodeQuery);
        return null;
    }

    @Factory
    public static Matcher<Parent> hasChildren(int amount, String nodeQuery) {
        return null;
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean hasChild(Parent parent) {
        return !parent.getChildrenUnmodifiable().isEmpty();
    }

    public static boolean hasChildren(int amount, Parent parent) {
        return parent.getChildrenUnmodifiable().size() == amount;
    }

}
