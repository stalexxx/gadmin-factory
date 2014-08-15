package com.ifree.common.gwt.client.actions;

import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;

/**
* Created by alex on 23.04.14.
*/
public interface Action<T> {
    String getDisplayText(@Nullable T item);

    boolean isEnabled(@Nullable T item);

    boolean isVisible(@Nullable T item);

    void perform(@Nullable T item);

    boolean isAdditional();

    Action<T> setAdditional(boolean additional);

    Action<T> action();

    boolean hasHistoryToken();

    String actualHistoryToken(@Nullable T item);

    IconType getDisplayIcon();
}