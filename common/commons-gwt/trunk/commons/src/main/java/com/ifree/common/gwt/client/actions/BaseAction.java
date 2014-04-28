package com.ifree.common.gwt.client.actions;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseAction<T> implements Action<T> {

    protected String text;

    public BaseAction(String text) {
        this.text = text;
    }

    @Override
    public String getDisplayText(T item) {
        return text;
    }
}
