package com.ifree.common.gwt.client.ui.lists;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseAction<T> implements Action<T> {

    private String text;

    public BaseAction(String text) {
        this.text = text;
    }

    @Override
    public String getDisplayText() {
        return text;
    }
}
