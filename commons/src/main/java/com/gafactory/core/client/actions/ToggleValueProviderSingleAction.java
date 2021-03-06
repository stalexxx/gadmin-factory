package com.gafactory.core.client.actions;

import com.gafactory.core.shared.ValueProvider;

/**
 * Created by alex on 25.04.14.
 */
public abstract class ToggleValueProviderSingleAction<T> extends ToggleSingleItemAction<T> {
    private ValueProvider<T, Boolean> valueProvider;

    public ToggleValueProviderSingleAction(ValueProvider<T, Boolean> valueProvider, String isTrueText, String isFalseText) {
        super(isTrueText, isFalseText,false);
        this.valueProvider = valueProvider;
    }

    public ToggleValueProviderSingleAction(ValueProvider<T, Boolean> valueProvider, String isTrueText, String isFalseText, boolean additional) {
        super(isTrueText, isFalseText, additional);
        this.valueProvider = valueProvider;
    }

    @Override
    protected boolean isToggled(T item) {
        Boolean value = item != null ? valueProvider.getValue(item): null;

        boolean isActiveNow = value != null ? value : false;
        return !isActiveNow;
    }


}
