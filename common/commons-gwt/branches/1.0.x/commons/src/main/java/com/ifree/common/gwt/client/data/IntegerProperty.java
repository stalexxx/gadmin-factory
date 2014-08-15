package com.ifree.common.gwt.client.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by alex on 05.08.14.
 */
public class IntegerProperty extends StorageService.Property<Integer> {
    public IntegerProperty(String name) {
        super(name);
    }

    @Override
    public String convertToString(@Nullable Integer obj) {
        return obj != null ? String.valueOf(obj) : null;
    }

    @Override
    protected Integer parseNonNull(@Nonnull String value) {
        return Integer.parseInt(value);
    }
}
