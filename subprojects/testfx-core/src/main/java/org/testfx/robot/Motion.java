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
package org.testfx.robot;

/**
 * Enumeration holding the three simplest types of motion between two 2D points
 * a = (x₁, y₁) and b = (x₂, y₂). Given any two points in the plane we can construct
 * a right-angled triangle where the hypotenuse is the straight-line between a and b.
 * <p>
 * <pre><code>
 * +-----------------------→ +x
 * |    d        b
 * |    |        *
 * |    |      * *
 * |    |    *   *
 * |    |  *     *
 * |    |*       *
 * |    **********
 * |    a        c
 * |
 * v
 * +y
 * </code></pre>
 * <p>
 * Traveling in a straight-line between a and b (that is, tracing the hypotenuse) is
 * {@code DIRECT}. Traveling first from a to c and then from c to b is {@code HORIZONTAL_FIRST}.
 * Traveling first from a to d and then from d to b is {@code VERTICAL_FIRST}. {@code DIRECT}
 * means that no specific type of motion was explicitly requested.
 */
public enum Motion {
    DEFAULT,
    DIRECT,
    HORIZONTAL_FIRST,
    VERTICAL_FIRST,
}
