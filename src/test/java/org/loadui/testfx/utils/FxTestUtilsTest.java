package org.loadui.testfx.utils;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FxTestUtilsTest {

    @Test
    public void intersection()
    {
        Bounds b1 = new BoundingBox(0,0,10,10);
        Bounds b2 = new BoundingBox(8,8,20,40);
        Bounds i = FXTestUtils.intersection(b1, b2);
        assertThat(i.getMinX(), is(8.0));
        assertThat(i.getMinY(), is(8.0));
        assertThat(i.getMaxX(), is(10.0));
        assertThat(i.getMaxY(), is(10.0));
    }
}
