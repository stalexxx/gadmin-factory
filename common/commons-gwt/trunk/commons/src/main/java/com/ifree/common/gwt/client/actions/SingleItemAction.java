package com.ifree.common.gwt.client.actions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by alex on 23.04.14.
 */
public abstract class SingleItemAction<T> extends BaseAction<T> {
    public SingleItemAction(String text) {
        super(text, false);
    }

    protected SingleItemAction(String text, boolean additional) {
        super(text, additional);
    }

    @Override
    public final boolean isEnabled(T item) {
        return item != null;
    }


    @Override
    public final void perform(@Nullable T item) {
        if (item != null) {
            nonNullPerform(item);
        }
    }



    protected abstract void nonNullPerform(@Nonnull T item);
}
