/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.framework.junit5;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.testfx.framework.junit5.utils.FXUtils;

/**
 * Simple JUnit 5 extension to ensure that {@code @Test} statements are executed
 * in the JavaFX UI thread. This is (strictly) necessary when testing setter
 * and/or getter methods of JavaFX classes (ie. Node derived, properties etc).
 * <p>
 * Use the
 * <ul>
 * <li>{@code @ExtendWith(avaFxInterceptor.class) } if all @Test, or
 * <li>{@code @ExtendWith(SelectiveJavaFxInterceptor.class) } if only @Test
 * + @TestFx annotated tests
 * </ul>
 * should be executed within the JavaFX UI thread.
 */
public class JavaFXInterceptorUtils {
    /**
     * Simple JUnit 5 extension to ensure that {@code @Test} statements are executed
     * in the JavaFX UI thread. This is (strictly) necessary when testing setter
     * and/or getter methods of JavaFX classes (ie. Node derived, properties etc).
     * <p>
     * Example usage:
     *
     * <pre>
     * <code>
     * &#64;ExtendWith(ApplicationExtension.class)
     * &#64;ExtendWith(JavaFxInterceptor.class)
     * public class SquareButtonTest {
     *     &#64;Start
     *     public void start(Stage stage) {
     *         // usual FX initialisation
     *         // ...
     *     }
     *
     *    &#64;TestFx
     *    // note: this is equivalent to {@code @Test} when using {@code @ExtendWith(JavaFxInterceptor.class)}
     *    public void testJavaFxThreadSafety() {
     *        // verifies that this test is indeed executed in the JavaFX thread
     *        assertTrue(Platform.isFxApplicationThread());
     *
     *        // perform the regular JavaFX thread safe assertion tests
     *        // ...
     *    }
     *
     *    &#64;Test // also executed in JavaFX thread,
     *    // for different behaviour use:  {@code @ExtendWith(SelectiveJavaFxInterceptor.class)}
     *    public void testNonJavaFx() {
     *        // verifies that this test is also executed in the JavaFX thread
     *        assertTrue(Platform.isFxApplicationThread());
     *
     *        // perform regular assertion tests within the JavaFX thread
     *        // ...
     *    }
     * }
     *
     * </code>
     * </pre>
     */
    public static class JavaFxInterceptor implements InvocationInterceptor {
        @Override
        public void interceptTestMethod(final Invocation<Void> invocation,
                final ReflectiveInvocationContext<Method> invocationContext,
                final ExtensionContext extensionContext) throws Throwable {
            final AtomicReference<Throwable> throwable = new AtomicReference<>();

            // N.B. explicit run and wait since the test should only continue
            // if the previous JavaFX access as been finished.
            FXUtils.runAndWait(() -> {
                try {
                    // executes function after @Test
                    invocation.proceed();
                }
                catch (final Throwable t) {
                    throwable.set(t);
                }
            });
            final Throwable t = throwable.get();
            if (t != null) {
                throw t;
            }
        }
    }

    /**
     * Simple JUnit 5 extension to ensure that {@code @Test} statements are executed
     * in the JavaFX UI thread. This is (strictly) necessary when testing setter
     * and/or getter methods of JavaFX classes (ie. Node derived, properties etc).
     * <p>
     * Example usage:
     *
     * <pre>
     * <code>
     * &#64;ExtendWith(ApplicationExtension.class)
     * &#64;ExtendWith(SelectiveJavaFxInterceptor.class)
     * public class SquareButtonTest {
     *     &#64;Start
     *     public void start(Stage stage) {
     *         // usual FX initialisation
     *         // ...
     *     }
     *
     *    &#64;TestFx // forces execution in JavaFX thread
     *    public void testJavaFxThreadSafety() {
     *        // verifies that this test is indeed executed in the JavaFX thread
     *        assertTrue(Platform.isFxApplicationThread());
     *
     *        // perform the regular JavaFX thread safe assertion tests
     *        // ...
     *    }
     *
     *    &#64;Test // explicitly not executed in JavaFX thread;
     *    // for different behaviour use:  {@code @ExtendWith(JavaFxInterceptor.class)}
     *    public void testNonJavaFx() {
     *        // verifies that this test is not executed within the JavaFX thread
     *        assertFalse(Platform.isFxApplicationThread());
     *
     *        // perform the regular non-JavaFX thread-related assertion tests
     *        // ...
     *    }
     * }
     *
     * </code>
     * </pre>
     */
    public static class SelectiveJavaFxInterceptor implements InvocationInterceptor {
        @Override
        public void interceptTestMethod(final Invocation<Void> invocation,
                final ReflectiveInvocationContext<Method> invocationContext,
                final ExtensionContext extensionContext) throws Throwable {
            final AtomicReference<Throwable> throwable = new AtomicReference<>();

            boolean isFxAnnotation = false;
            final Optional<AnnotatedElement> element = extensionContext.getElement();
            if (element.isPresent()) {
                for (final Annotation annotation : element.get().getAnnotations()) {
                    if (annotation.annotationType().equals(TestFx.class)) {
                        isFxAnnotation = true;
                    }
                }
            }

            final Runnable testToBeExecuted = () -> {
                try {
                    // executes function after @Test
                    invocation.proceed();
                }
                catch (final Throwable t) {
                    throwable.set(t);
                }
            };

            // N.B. explicit run and wait since the test should only continue
            // if the previous JavaFX access as been finished.
            if (isFxAnnotation) {
                FXUtils.runAndWait(testToBeExecuted);
            } else {
                testToBeExecuted.run();
            }
            final Throwable t = throwable.get();
            if (t != null) {
                throw t;
            }
        }
    }
}
