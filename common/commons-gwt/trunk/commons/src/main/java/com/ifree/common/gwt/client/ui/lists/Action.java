package com.ifree.common.gwt.client.ui.lists;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
* Created by alex on 23.04.14.
*/
public interface Action<T> {
    String getDisplayText();

    boolean isEnabled(@Nullable T item);

    void perform(@Nullable T item);
}
