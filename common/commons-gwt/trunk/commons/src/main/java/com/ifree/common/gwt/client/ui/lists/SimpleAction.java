package com.ifree.common.gwt.client.ui.lists;

/**
 * Created by alex on 23.04.14.
 */
public abstract class SimpleAction<T> extends BaseAction<T> {
    public SimpleAction(String text) {
        super(text);
    }

    @Override
    public final boolean isEnabled(T item) {
        return true;
    }


}