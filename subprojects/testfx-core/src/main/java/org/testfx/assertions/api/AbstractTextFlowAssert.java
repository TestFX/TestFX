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
package org.testfx.assertions.api;

import javafx.scene.text.TextFlow;

import org.testfx.matcher.control.TextFlowMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.text.TextFlow} assertions.
 */
public class AbstractTextFlowAssert<SELF extends AbstractTextFlowAssert<SELF>> extends AbstractParentAssert<SELF> {

    protected AbstractTextFlowAssert(TextFlow actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} has exactly the given {@code text}
     * (the result of combining all of its text-based children's text together).
     *
     * @param text the given text to compare the actual text to
     * @return this assertion object
     */
    public SELF hasText(String text) {
        assertThat(actual).is(fromMatcher(TextFlowMatchers.hasText(text)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} does not have exactly the given
     * {@code text} (the result of combining all of its text-based children's text together).
     *
     * @param text the given text to compare the actual text to
     * @return this assertion object
     */
    public SELF doesNotHaveText(String text) {
        assertThat(actual).is(fromInverseMatcher(TextFlowMatchers.hasText(text)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} has the given {@code coloredTextMarkup}.
     * The color is matched by using the closest named color, as described below.
     * <p>
     * Colors are specified using the following markup:
     * <pre>{@code <COLOR>text</COLOR>}</pre>
     * <p>
     * Where {@literal COLOR} is one of JavaFX's named colors.
     * <p>
     * Here is an example for verifying that a TextFlow contains the text
     * "hello" and that the named color that has the closest value to the
     * color of the text is {@literal Colors.RED}:
     * <p>
     * <pre>{@code
     *   Text text = new Text("hello");
     *   text.setFill(Colors.RED);
     *   TextFlow textFlow = new TextFlow(text);
     *   assertThat(textFlow).hasColoredText("<RED>hello</RED>");
     * }</pre>
     *
     * @param coloredTextMarkup the given colored text markup to compare the actual colored text to
     * @return this assertion object
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
     */
    public SELF hasColoredText(String coloredTextMarkup) {
        assertThat(actual).is(fromMatcher(TextFlowMatchers.hasColoredText(coloredTextMarkup)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} does not have the given {@code coloredTextMarkup}.
     * The color is matched by using the closest named color, as described below.
     * <p>
     * Colors are specified using the following markup:
     * <pre>{@code <COLOR>text</COLOR>}</pre>
     * <p>
     * Where {@literal COLOR} is one of JavaFX's named colors.
     * <p>
     * Here is an example for verifying that a TextFlow does not contain the text
     * "hello" with any color that has the closest named color {@literal Colors.RED}:
     * <p>
     * <pre>{@code
     *   Text text = new Text("hello");
     *   text.setFill(Colors.BLUE);
     *   TextFlow textFlow = new TextFlow(text);
     *   assertThat(textFlow).doesNotHaveColoredText("<RED>hello</RED>");
     * }</pre>
     *
     * @param coloredTextMarkup the given colored text markup to compare the actual colored text to
     * @return this assertion object
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
     */
    public SELF doesNotHaveColoredText(String coloredTextMarkup) {
        assertThat(actual).is(fromInverseMatcher(TextFlowMatchers.hasColoredText(coloredTextMarkup)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} has exactly the given {@code coloredTextMarkup}.
     * The color is matched in an exact way, as described below.
     * <p>
     * Colors are specified using the following markup:
     * <pre>{@code <COLOR>text</COLOR>}</pre>
     * <p>
     * Where {@literal COLOR} is one of JavaFX's named colors.
     * <p>
     * Here is an example for verifying that a TextFlow contains the text
     * "hello" and that the color of the text is <em>exactly</em> {@literal Colors.BLUE}
     * (that is, it has an RGB value of (0, 0, 255)).
     *
     * <pre><code>
     *   Text text = new Text("hello");
     *   text.setFill(Colors.BLUE); // or: text.setFill(Colors.rgb(0, 0, 255));
     *   TextFlow textFlow = new TextFlow(text);
     *   assertThat(textFlow).hasExactlyColoredText("<BLUE>hello</BLUE>");
     * </code></pre>
     *
     * @param coloredTextMarkup the given colored text markup to compare the actual colored text to
     * @return this assertion object
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
     */
    public SELF hasExactlyColoredText(String coloredTextMarkup) {
        assertThat(actual).is(fromMatcher(TextFlowMatchers.hasExactlyColoredText(coloredTextMarkup)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.text.TextFlow} does not have exactly the given
     * {@code coloredTextMarkup}. The color is matched in an exact way, as described below.
     * <p>
     * Colors are specified using the following markup:
     * <pre>{@code <COLOR>text</COLOR>}</pre>
     * <p>
     * Where {@literal COLOR} is one of JavaFX's named colors.
     * <p>
     * Here is an example for verifying that a TextFlow does not contain the text
     * "hello" and that the color of the text is not <em>exactly</em> {@literal Colors.BLUE}
     * (that is, it does not have an RGB value of (0, 0, 255)).
     *
     * <pre><code>
     *   Text text = new Text("hello");
     *   text.setFill(Colors.rgb(0, 0, 254));
     *   TextFlow textFlow = new TextFlow(text);
     *   assertThat(textFlow).doesNotHaveExactlyColoredText("<BLUE>hello</BLUE>");
     * </code></pre>
     *
     * @param coloredTextMarkup the given colored text markup to compare the actual colored text to
     * @return this assertion object
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
     */
    public SELF doesNotHaveExactlyColoredText(String coloredTextMarkup) {
        assertThat(actual).is(fromMatcher(TextFlowMatchers.hasExactlyColoredText(coloredTextMarkup)));
        return myself;
    }
}
