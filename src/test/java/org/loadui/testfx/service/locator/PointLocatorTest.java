package org.loadui.testfx.service.locator;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PointLocatorTest extends GuiTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    PointLocator pointLocator;

    Pane pane;
    Node node;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        pointLocator = new PointLocator(new BoundsLocator());
    }

    @Override
    protected Parent getRootNode() {
        node = new Label("node");
        pane = new AnchorPane();
        pane.getChildren().setAll(node);
        return pane;
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void shouldRetrievePointForOffset() {
        // given:
        Point2D origin = pointLocator.pointFor(pane).atOffset(0, 0);
        PointQuery pointQuery = pointLocator.pointFor(node);

        // when:
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        assertThat(point, equalTo(addToPoint(origin, 0, 0)));
    }

    @Test
    public void shouldRetrievePointForOffsetAfterUpdate() {
        // given:
        Point2D origin = pointLocator.pointFor(pane).atOffset(0, 0);
        PointQuery pointQuery = pointLocator.pointFor(node);

        // when:
        node.setLayoutX(100);
        node.setLayoutY(100);
        Point2D point = pointQuery.atOffset(0, 0);

        // then:
        assertThat(point, equalTo(addToPoint(origin, 100, 100)));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    public Point2D addToPoint(Point2D point, double x, double y) {
        return new Point2D(point.getX() + x, point.getY() + y);
    }

}
