/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.api;

import org.testfx.service.finder.NodeFinder;
import org.testfx.service.support.CaptureSupport;

/**
 * Stores contextual information for {@link FxAssert}:
 * <ul>
 *     <li>a {@link NodeFinder}, which defaults to {@link FxServiceContext#getNodeFinder()}</li>
 *     <li>a {@link CaptureSupport}, which defaults to {@link FxServiceContext#getCaptureSupport()}</li>
 * </ul>
 */
public class FxAssertContext {

    private NodeFinder nodeFinder = FxService.serviceContext().getNodeFinder();
    private CaptureSupport captureSupport = FxService.serviceContext().getCaptureSupport();

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public void setNodeFinder(NodeFinder nodeFinder) {
        this.nodeFinder = nodeFinder;
    }

    public CaptureSupport getCaptureSupport() {
        return captureSupport;
    }

    public void setCaptureSupport(CaptureSupport captureSupport) {
        this.captureSupport = captureSupport;
    }

}
