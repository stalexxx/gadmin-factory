package com.ifree.common.gwt.client.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by alex on 05.08.14.
 */
public class TextProperty<T> extends StorageService.Property<String> {
    protected TextProperty(String name) {
        super(name);
    }

    @Override
    public String convertToString(@Nullable String obj) {
        return obj;
    }

    @Override
    protected String parseNonNull(@Nonnull String value) {
        return value;
    }
}
