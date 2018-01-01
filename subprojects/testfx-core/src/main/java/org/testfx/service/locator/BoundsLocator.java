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
package org.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public interface BoundsLocator {

    /**
     *
     * @param node the node
     * @return the visible bounds (in terms of its Scene) of the node, which is limited by the bounds of its
     * {@code Scene}. If the node's bounds extend beyond the Scene's bounds, the excess will be removed.
     */
    Bounds boundsInSceneFor(Node node);

    /**
     *
     * @param scene the scene
     * @return the bounds of the scene
     */
    Bounds boundsInWindowFor(Scene scene);

    /**
     *
     * @param boundsInScene the bounds, which may extend beyond the scene's bounds
     * @param scene the scene used to set the bounds limits
     * @return the visible bounds (in terms of the Scene's Window) of the given {@code boundsInScene}, which is limited
     * by the bounds of the given {@code Scene}. If the former extends beyond the latter, the excess will be removed
     */
    Bounds boundsInWindowFor(Bounds boundsInScene, Scene scene);

    /**
     *
     * @param node the node.
     * @return the visible bounds (in terms of the screen) of the node, which is limited first by its {@code Scene}'s
     * bounds and secondly by its Scene's {@code Window}'s bounds
     */
    Bounds boundsOnScreenFor(Node node);

    /**
     *
     * @param scene the scene
     * @return the visible bounds (in terms of the screen) of the given Scene
     */
    Bounds boundsOnScreenFor(Scene scene);

    /**
     *
     * @param window the window
     * @return the bounds of the given window
     */
    Bounds boundsOnScreenFor(Window window);

    /**
     *
     * @param boundsInScene the initial bounds to convert to screen bounds
     * @param scene the scene that limits the boundsInScene
     * @return the visible bounds (in terms of the screen) of the given {@code boundsInScene}, which is limited by the
     * given Scene's bounds.
     */
    Bounds boundsOnScreenFor(Bounds boundsInScene, Scene scene);
}
