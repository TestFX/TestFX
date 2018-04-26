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
module org.testfx {
    exports org.testfx.api;
    exports org.testfx.assertions.api;
    exports org.testfx.matcher.base;
    exports org.testfx.matcher.control;
    exports org.testfx.service.adapter;
    exports org.testfx.service.finder;
    exports org.testfx.service.locator;
    exports org.testfx.service.query;
    exports org.testfx.service.support;
    exports org.testfx.osgi;
    exports org.testfx.osgi.service;
    exports org.testfx.robot;
    exports org.testfx.toolkit;
    exports org.testfx.util;

    requires java.desktop;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires hamcrest.core;
    requires assertj.core;
}
