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
package org.testfx.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FxToolkitTest {

    @Test
    public void improve_common_runtime_exception_messages() {
        // given
        StackTraceElement element = new StackTraceElement(FxToolkit.UNSUPPORTED_OPERATION_CALLING_CLASS, "", "", 0);
        UnsupportedOperationException unsupportedOperationException =
                new UnsupportedOperationException(FxToolkit.UNSUPPORTED_OPERATION_ERROR_MESSAGE);
        unsupportedOperationException.setStackTrace(new StackTraceElement[]{element});
        RuntimeException exception = new RuntimeException(unsupportedOperationException);

        // when/then
        assertThatThrownBy(() -> FxToolkit.handleCommonRuntimeExceptions(exception))
                .hasMessage(FxToolkit.MISSING_LIBGTK_3_0_USER_MESSAGE);
    }

}
