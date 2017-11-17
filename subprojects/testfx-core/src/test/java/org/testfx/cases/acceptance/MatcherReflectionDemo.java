/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.testfx.cases.acceptance;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Ignore;

@Ignore("requires hamcrest-library")
public class MatcherReflectionDemo {

    public static void main(String[] args) {
        /*
        Matcher matcherIterable = Matchers.emptyIterable();
        Matcher matcherCollection = Matchers.empty();
        Matcher matcherArray = Matchers.emptyArray();
        Matcher matcherString = Matchers.isEmptyString();

        Type typeIterable = fetchGenericArgumentType(matcherIterable, 0);
        Type typeCollection = fetchGenericArgumentType(matcherCollection, 0);
        Type typeArray = fetchGenericArgumentType(matcherArray, 0);
        Type typeString = fetchGenericArgumentType(matcherString, 0);

        System.out.println(isAssignableToType(Set.class, typeIterable));
        System.out.println(isAssignableToType(Set.class, typeCollection));
        System.out.println(isAssignableToType(Set.class, typeArray));
        System.out.println(isAssignableToType(Set.class, typeString));
        */
    }

    private static Type fetchGenericArgumentType(Object instance,
                                                 int index) {
        Type genericType = instance.getClass().getGenericSuperclass();
        ParameterizedType genericType0 = (ParameterizedType) genericType;
        Type genericArgument = genericType0.getActualTypeArguments()[index];
        if (genericArgument instanceof ParameterizedType) {
            ParameterizedType genericArgument0 = (ParameterizedType) genericArgument;
            return genericArgument0.getRawType();
        }
        return genericArgument;
    }

    private static boolean isAssignableToType(Class<?> class0,
                                              Type type) {
        System.out.println(type);
        if (type instanceof Class<?>) {
            return ((Class<?>) type).isAssignableFrom(class0);
        }
        return false;
    }

}
