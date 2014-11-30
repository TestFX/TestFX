package org.testfx.matcher.base;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unchecked")
public class PredicateMatchersTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public Node nullNode = null;

    public Pane notMatchingNode = new Pane();

    public Cursor notNode = Cursor.DEFAULT;

    public Predicate<Node> notNullNodePredicate =
        node -> node != null;

    public Predicate<Parent> hasChildrenParentPredicate =
        parent -> parent.getChildrenUnmodifiable().size() > 0;

    @Rule
    public ExpectedException exception = ExpectedException.none().handleAssertionErrors();

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void uncheckedNodeMatcher_with_nullNode() {
        // given:
        Matcher notNullNodeMatcher = PredicateMatchers.nodeMatcher(
            "Node is not null", notNullNodePredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Node is not null\n" +
                                "     but: was null");
        assertThat(nullNode, notNullNodeMatcher);
    }

    @Test
    public void checkedNodeMatcher_with_notMatchingNode() {
        // given:
        Matcher hasChildrenParentMatcher = PredicateMatchers.nodeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was <" + notMatchingNode.toString() + ">");
        assertThat(notMatchingNode, hasChildrenParentMatcher);
    }

    @Test
    public void checkedNodeMatcher_with_nullNode() {
        // given:
        Matcher hasChildrenParentMatcher = PredicateMatchers.nodeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was null");
        assertThat(nullNode, hasChildrenParentMatcher);
    }

    // java.lang.ClassCastException: javafx.scene.control.Button cannot be cast to javafx.scene.control.TreeView

    @Test
    public void checkedNodeMatcher_with_notNode() {
        // given:
        Matcher hasChildrenParentMatcher = PredicateMatchers.nodeMatcher(
            Parent.class, "has children", hasChildrenParentPredicate
        );

        // expect:
        // TODO: Hint expected type on AssertError explicitly.
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Parent has children\n" +
                                "     but: was a javafx.scene.Cursor$StandardCursor (<DEFAULT>)");
        assertThat(notNode, hasChildrenParentMatcher);
    }

}
