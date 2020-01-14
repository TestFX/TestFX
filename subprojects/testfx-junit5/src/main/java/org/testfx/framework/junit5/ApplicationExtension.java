/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2019 The TestFX Contributors
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

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.extension.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class ApplicationExtension extends FxRobot implements BeforeEachCallback, AfterEachCallback,
        TestInstancePostProcessor, ParameterResolver {

    private ApplicationFixture applicationFixture;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Class<?> testClass = testInstance.getClass();

        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(FxRobot.class)) {
                setField(testInstance, field, this);
            }
        }
        applicationFixture = new AnnotationBasedApplicationFixture(
                testInstance,
                getMethods(testClass, Init.class, (method) -> validateInitMethod(method)),
                getMethods(testClass, Start.class, (method) -> validateStartMethod(method)),
                getMethods(testClass, Stop.class, (method) -> validateStartMethod(method))
        );
    }

    private Set<Pair<Method, String>> getMethods(Class<?> testClass, Class<? extends Annotation> annotation, Consumer<Method> validate) {
        final Set<Method> methods = getAllMethods(testClass, withAnnotation(annotation));

        final Set<Pair<Method, String>> methodPairs = new HashSet<>();

        for(Method m : methods) {
            validate.accept(m);
            methodPairs.add(new Pair(m, testClass.getName()));
        }

        if (isNested(testClass)) {
            methodPairs.addAll(getMethods(getParentClass(testClass), annotation, validate));
        }

        return methodPairs;
    }

    private boolean isNested(Class<?> testClass) {
        return testClass.getName().split("\\$").length > 1;
    }

    private Class<?> getParentClass(Class<?> testClass) {
        final String[] split = testClass.getName().split("\\$");
        final String parentClassName = Arrays.stream(Arrays.copyOf(split, split.length - 1)).collect(Collectors.joining("$"));

        try {
            return Class.forName(parentClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(FxRobot.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return this;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(applicationFixture));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        FxToolkit.cleanupApplication(new ApplicationAdapter(applicationFixture));
        // Cleaning the remaining UI events (e.g. a mouse press that is still waiting for a mouse release)
        // Not cleaning these events may have side-effects on the next UI tests
        release(new KeyCode[0]);
        release(new MouseButton[0]);
        // Required to wait for the end of the UI events processing
        WaitForAsyncUtils.waitForFxEvents();
    }

    private Method validateInitMethod(Method initMethod) {
        if (initMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Method annotated with @Init should have no arguments");
        }
        return initMethod;
    }

    private Method validateStartMethod(Method startMethod) {
        Class<?>[] parameterTypes = startMethod.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].isAssignableFrom(javafx.stage.Stage.class)) {
            throw new IllegalStateException("Method annotated with @Start should have one argument of type " +
                    "javafx.stage.Stage");
        }
        return startMethod;
    }

    private Method validateStopMethod(Method stopMethod) {
        if (stopMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Method annotated with @Stop should have no arguments");
        }
        return stopMethod;
    }

    private void setField(Object instance, Field field, Object val) throws IllegalAccessException {
        boolean wasAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, val);
        } finally {
            field.setAccessible(wasAccessible);
        }
    }

    private static class AnnotationBasedApplicationFixture implements ApplicationFixture {
        static Map<String, Object> TEST_INSTANCES = new HashMap<>();
        private final Set<Pair<Method, String>> init;
        private final Set<Pair<Method, String>> start;
        private final Set<Pair<Method, String>> stop;

        private AnnotationBasedApplicationFixture(Object testInstance, Set<Pair<Method, String>> init,
                                                  Set<Pair<Method, String>> start, Set<Pair<Method, String>> stop) {
            TEST_INSTANCES.put(testInstance.getClass().getName(), testInstance);
            this.init = init;
            this.start = start;
            this.stop = stop;
        }

        @Override
        public void init() throws InvocationTargetException, IllegalAccessException {
            for (Pair<Method, String> pair : init) {
                pair.getKey().invoke(TEST_INSTANCES.get(pair.getValue()));
            }
        }

        @Override
        public void start(Stage stage) throws InvocationTargetException, IllegalAccessException {
            for (Pair<Method, String> pair : start) {
                pair.getKey().invoke(TEST_INSTANCES.get(pair.getValue()), stage);
            }
        }

        @Override
        public void stop() throws InvocationTargetException, IllegalAccessException {
            for (Pair<Method, String> pair : stop) {
                pair.getKey().invoke(TEST_INSTANCES.get(pair.getValue()));
            }
        }
    }
}
