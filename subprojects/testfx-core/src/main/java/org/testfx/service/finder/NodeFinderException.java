/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.finder;

public class NodeFinderException extends RuntimeException {

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ErrorType errorType;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public NodeFinderException(String message,
                               ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public ErrorType getErrorType() {
        return errorType;
    }

    //---------------------------------------------------------------------------------------------
    // STATIC INNER CLASSES.
    //---------------------------------------------------------------------------------------------

    public enum ErrorType {
        NO_NODES_FOUND,
        NO_VISIBLE_NODES_FOUND
    }

}
