package org.testfx.api;

import com.google.common.annotations.Beta;

@Beta
public class FxSelector {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final FxSelectorContext context = new FxSelectorContext();

    //---------------------------------------------------------------------------------------------
    // PRIVATE CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FxSelector() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static FxSelectorContext selectorContext() {
        return context;
    }

}
