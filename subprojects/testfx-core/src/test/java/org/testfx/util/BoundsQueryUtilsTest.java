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
package org.testfx.util;

import java.util.concurrent.TimeoutException;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.testfx.api.FxAssert.verifyThat;

public class BoundsQueryUtilsTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    private static Shape shape;
    private static Region region;
    private static Scene scene;
    private static Stage stage;

    private static final double SHAPE_WIDTH = 200;
    private static final double CLIP_WIDTH = 100;
    private static final double TRANSLATE_X = 50;
    private static final double LAYOUT_X = 25;
    private static final double PADDING_LEFT = 200;
    private static final double BORDER_LEFT = 100;
    private static final double MARGIN_LEFT = 50;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> {
            setupShape();
            setupRegion();
            setupScene();
            setupStage();
        });
    }

    private static void setupShape() {
        shape = new Rectangle(0, 0, SHAPE_WIDTH, 0); // nodeBounds()
        shape.setClip(new Rectangle(0, 0, CLIP_WIDTH, 0)); // nodeBoundsInLocal()
        shape.getTransforms().add(new Translate(TRANSLATE_X, 0)); // nodeBoundsInParent()

        Shape altShape = new Rectangle(0, 0, SHAPE_WIDTH, 0); // nodeBounds()
        altShape.setFill(Color.GREEN);
        altShape.setStroke(Color.BLACK);
        altShape.setStrokeType(StrokeType.OUTSIDE);
        altShape.setStrokeWidth(5); // nodeBounds(), nodeBoundsInParent()
        altShape.setEffect(new BoxBlur(10, 10, 1)); // nodeBoundsInLocal()
    }

    private static void setupRegion() {
        region = new Region();
        region.setMaxSize(0, 0);
        region.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        region.setPadding(new Insets(0, 0, 0, PADDING_LEFT)); // nodeBounds(), nodeBoundsInLocal()
        region.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null,
                new BorderWidths(0, 0, 0, BORDER_LEFT)))); // nodeBounds(), nodeBoundsInLocal()
        StackPane.setMargin(region, new Insets(0, 0, 0, MARGIN_LEFT)); // nodeBoundsInParent()
    }

    private static void setupScene() {
        StackPane sceneRoot = new StackPane(shape, region);
        sceneRoot.setAlignment(Pos.TOP_LEFT);
        sceneRoot.setLayoutX(LAYOUT_X); // bounds(Scene)
        sceneRoot.setLayoutY(0);
        scene = new Scene(sceneRoot, 1, 1);
    }

    private static void setupStage() {
        // XXX: the real x and y are not set immediately. they are managed by the operating system.
        stage = new Stage(StageStyle.UNDECORATED);
        // stage.setX(0); // bounds(Window)
        // stage.setY(0);
        stage.setScene(scene);
        stage.show();
    }

    @AfterClass
    public static void cleanupStage() throws TimeoutException {
        FxToolkit.setupFixture(() -> stage.close());
    }

    @Test
    public void bounds_doubles() {
        verifyThat(BoundsQueryUtils.bounds(1, 2, 3, 4), hasBounds(1, 2, 3, 4));
    }

    @Test
    public void bounds_point() {
        verifyThat(BoundsQueryUtils.bounds(new Point2D(1, 2)), hasBounds(1, 2, 0, 0));
    }

    @Test
    public void bounds_dimension() {
        verifyThat(BoundsQueryUtils.bounds(new Dimension2D(1, 2)), hasBounds(0, 0, 1, 2));
    }

    @Test
    public void bounds_rectangle() {
        verifyThat(BoundsQueryUtils.bounds(new Rectangle2D(1, 2, 3, 4)), hasBounds(1, 2, 3, 4));
    }

    @Test
    public void bounds_scene() {
        verifyThat(BoundsQueryUtils.bounds(scene), hasBounds(0, 0, 1, 1));
    }

    @Test
    public void bounds_window() {
        verifyThat(BoundsQueryUtils.bounds(stage), hasBounds(stage.getX(), stage.getY(), 1, 1));
    }

    @Test
    public void nodeBounds_shape() {
        Bounds bounds = BoundsQueryUtils.nodeBounds(shape);
        verifyThat(bounds, hasBounds(0, 0, SHAPE_WIDTH, 0));
    }

    @Test
    public void nodeBoundsInLocal_shape() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInLocal(shape);
        verifyThat(bounds, hasBounds(0, 0, CLIP_WIDTH, 0));
    }

    @Test
    public void nodeBoundsInParent_shape() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInParent(shape);
        verifyThat(bounds, hasBounds(TRANSLATE_X, 0, CLIP_WIDTH, 0));
    }

    @Test
    public void nodeBoundsInScene_shape() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInScene(shape);
        verifyThat(bounds, hasBounds(LAYOUT_X + TRANSLATE_X, 0, CLIP_WIDTH, 0));
    }

    @Test
    public void nodeBounds_region() {
        Bounds bounds = BoundsQueryUtils.nodeBounds(region);
        verifyThat(bounds, hasBounds(0, 0, BORDER_LEFT + PADDING_LEFT, 0));
    }

    @Test
    public void nodeBoundsInLocal_region() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInLocal(region);
        verifyThat(bounds, hasBounds(0, 0, BORDER_LEFT + PADDING_LEFT, 0));
    }

    @Test
    public void nodeBoundsInParent_region() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInParent(region);
        verifyThat(bounds, hasBounds(MARGIN_LEFT, 0, BORDER_LEFT + PADDING_LEFT, 0));
    }

    @Test
    public void nodeBoundsInScene_region() {
        Bounds bounds = BoundsQueryUtils.nodeBoundsInScene(region);
        verifyThat(bounds, hasBounds(LAYOUT_X + MARGIN_LEFT, 0, BORDER_LEFT + PADDING_LEFT, 0));
    }

    @Test
    public void boundsOnScreen_screen() {
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(new BoundingBox(1, 2, 3, 4), Screen.getPrimary().getBounds());
        verifyThat(bounds, hasBounds(1, 2, 3, 4));
    }

    @Test
    public void boundsOnScreen_window() {
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(new BoundingBox(1, 2, 3, 4), stage);
        verifyThat(bounds, hasBounds(stage.getX() + 1, stage.getY() + 2, 3, 4));
    }

    @Test
    public void boundsOnScreen_scene() {
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(new BoundingBox(1, 2, 3, 4), scene);
        verifyThat(bounds, hasBounds(stage.getX() + 1, stage.getY() + 2, 3, 4));
    }

    @Test
    public void boundsOnScreen_shape() {
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(shape);
        verifyThat(bounds, hasBounds(stage.getX() + LAYOUT_X + TRANSLATE_X, stage.getY(), CLIP_WIDTH, 0));
    }

    @Test
    public void boundsOnScreen_region() {
        Bounds bounds = BoundsQueryUtils.boundsOnScreen(region);
        verifyThat(bounds, hasBounds(stage.getX() + LAYOUT_X + MARGIN_LEFT, stage.getY(),
                BORDER_LEFT + PADDING_LEFT, 0));
    }

    private Matcher<Bounds> hasBounds(double minX, double minY, double width, double height) {
        return CoreMatchers.is(new BoundingBox(minX, minY, width, height));
    }

}
