package com.gafactory.core.client.data;

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
        return !Objects.equals(value, "null") ? safeParse(value) : null;
    }

    private Integer safeParse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
