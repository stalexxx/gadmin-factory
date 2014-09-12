package com.ifree.common.gwt.client.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by alex on 05.08.14.
 */
public class IntegerProperty extends StorageService.Property<Integer> {
    public IntegerProperty(String name) {
        super(name);
    }

    @Override
    public String convertToString(@Nullable Integer obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    @Override
    protected Integer parseNonNull(@Nonnull String value) {
        return !Objects.equals(value, "null") ? Integer.parseInt(value) : null;
    }
}
