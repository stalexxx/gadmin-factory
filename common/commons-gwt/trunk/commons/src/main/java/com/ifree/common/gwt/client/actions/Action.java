package com.ifree.common.gwt.client.actions;

import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;

/**
* Created by alex on 23.04.14.
*/
public interface Action<T> {

    public enum ACTION_TYPE {
        LINK,
        LINK_BLANK,
        HISTORY_TOKEN,
        SCRIPT
    }

    String getDisplayText(@Nullable T item);

    boolean isEnabled(@Nullable T item);

    boolean isVisible(@Nullable T item);

    void perform(@Nullable T item);

    boolean isAdditional();

    Action<T> setAdditional(boolean additional);

    Action<T> action();

    ACTION_TYPE getType();

    String actualHistoryTokenOrLink(@Nullable T item);

    IconType getDisplayIcon();
}
