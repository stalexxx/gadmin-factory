package com.ifree.common.gwt.client.ui.lists;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Created by alex on 23.04.14.
 */
public abstract class SingleItemAction<T> extends BaseAction<T> {
    public SingleItemAction(String text) {
        super(text);
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

    protected abstract void nonNullPerform(@NotNull T item);
}
