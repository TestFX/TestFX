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
package org.testfx.robot.impl;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.ScrollRobot;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScrollRobotImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    ScrollRobot scrollRobot;
    MouseRobot mouseRobot;

    @Before
    public void setup() {
        mouseRobot = mock(MouseRobot.class);
        scrollRobot = new ScrollRobotImpl(mouseRobot);
    }

    @Test
    public void scrollPositive() {
        // when:
        scrollRobot.scroll(5);

        verify(mouseRobot, times(5)).scroll(eq(1));
    }

    @Test
    public void scrollNegative() {
        // when:
        scrollRobot.scroll(-5);

        verify(mouseRobot, times(5)).scroll(eq(-1));
    }

    @Test
    public void scrollUp() {
        // when:
        scrollRobot.scrollUp(5);

        // then:
        verify(mouseRobot, times(5)).scroll(eq(-1));
    }

    @Test
    public void scrollVerticalUp() {
        // when:
        scrollRobot.scroll(3, VerticalDirection.UP);

        // then:
        verify(mouseRobot, times(3)).scroll(eq(-1));
    }

    @Test
    public void scrollVerticalDown() {
        // when:
        scrollRobot.scroll(3, VerticalDirection.DOWN);

        // then:
        verify(mouseRobot, times(3)).scroll(eq(1));
    }

    @Test
    public void scrollHorizontalLeft() {
        // when:
        scrollRobot.scroll(4, HorizontalDirection.LEFT);

        // then:
        verify(mouseRobot, times(4)).scroll(eq(-1));
    }

    @Test
    public void scrollHorizontalRight() {
        // when:
        scrollRobot.scroll(6, HorizontalDirection.RIGHT);

        // then:
        verify(mouseRobot, times(6)).scroll(eq(1));
    }

    @Test
    public void scrollDown() {
        // when:
        scrollRobot.scrollDown(5);

        // then:
        verify(mouseRobot, times(5)).scroll(eq(1));
    }

    @Test
    public void scrollLeft() {
        // when:
        scrollRobot.scrollLeft(5);

        // then:
        verify(mouseRobot, times(5)).scroll(eq(-1));
    }

    @Test
    public void scrollRight() {
        // when:
        scrollRobot.scrollRight(5);

        // then:
        verify(mouseRobot, times(5)).scroll(eq(1));
    }
}
