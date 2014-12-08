package org.testfx.service.query.impl;

import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.NodeQueryFactory;

public class NodeQueryFactoryImpl implements NodeQueryFactory {

    @Override
    public NodeQuery build() {
        return new NodeQueryImpl();
    }

}
