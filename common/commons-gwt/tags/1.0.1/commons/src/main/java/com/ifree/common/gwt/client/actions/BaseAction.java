package com.ifree.common.gwt.client.actions;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseAction<T> implements Action<T> {

    protected String text;
    private boolean additional;

    public BaseAction(String text) {
        this.text = text;
    }

    protected BaseAction(String text, boolean additional) {
        this.text = text;
        this.additional = additional;
    }

    @Override
    public String getDisplayText(T item) {
        return text;
    }

    @Override
    public boolean isAdditional() {
        return additional;
    }

    @Override
    public Action<T> setAdditional(boolean additional) {
        this.additional = additional;
        return this;
    }

    @Override
    public Action<T> action() {
        return this;
    }


}
