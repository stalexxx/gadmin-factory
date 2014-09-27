package com.ifree.common.gwt.client.actions;

import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseAction<T> implements Action<T> {

    String text;
    private boolean additional;
    private IconType iconType;

    BaseAction(String text) {
        this.text = text;
    }

    BaseAction(String text, boolean additional) {
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
    public Action<T> setAdditional() {
        this.additional = true;
        return this;
    }

    @Override
    public Action<T> action() {
        return this;
    }

    @Override
    public ACTION_TYPE getType() {
        return ACTION_TYPE.SCRIPT;
    }

    @Override
    public String actualHistoryTokenOrLink(@Nullable T item) {
        return null;
    }

    @Override
    public IconType getDisplayIcon() {
        return iconType;
    }


    public void setIconType(IconType iconType) {
        this.iconType = iconType;
    }
}
