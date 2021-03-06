package com.gafactory.core.client.actions;

import javax.annotation.Nullable;

/**
 * Created by alex on 25.04.14.
 */
public abstract class SingleItemAlwaysVisibleAction<T> extends SingleItemAction<T> {
    public SingleItemAlwaysVisibleAction(String text) {
        super(text, false);
    }

    protected SingleItemAlwaysVisibleAction(String text, boolean additional) {
        super(text, additional);
    }

    @Override
    public boolean isVisible(@Nullable T item) {
        return true;
    }


}
