package org.loadui.testfx.service.finder;

import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;

public interface NodeFinder {
    public void      target(Window window);
    public void      target(int windowNumber);
    public void      target(String stageTitleRegex);
    public void      target(Scene scene);

    public Node      node(String query);
    public Set<Node> nodes(String query);
    public Node      node(Predicate<Node> predicate);
    public Set<Node> nodes(Predicate<Node> predicate);
    public Node      node(Matcher<Object> matcher);
    public Set<Node> nodes(Matcher<Object> matcher);

    public Node      parent(Window window);
    public Node      parent(int windowNumber);
    public Node      parent(String stageTitleRegex);
    public Node      parent(Scene scene);

    public Node      node(String query, Node parentNode);
    public Set<Node> nodes(String query, Node parentNode);
}
