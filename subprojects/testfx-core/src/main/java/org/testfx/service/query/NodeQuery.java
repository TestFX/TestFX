package org.testfx.service.query;

import java.util.Set;
import javafx.scene.Node;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public interface NodeQuery {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public NodeQuery from(Node... parentNodes);

    public NodeQuery from(Set<Node> parentNodes);

    public NodeQuery lookup(Function<Node, Set<Node>> selector);

    public NodeQuery lookupAt(int index,
                              Function<Node, Set<Node>> selector);

    public NodeQuery match(Predicate<Node> filter);

    public NodeQuery throwIfEmpty();

    public Set<Node> queryAll();

    public Optional<Node> queryFirst();

}
