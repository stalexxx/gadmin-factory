package com.ifree.common.gwt.shared;

/**
 * Created by alex on 25.06.14.
 */
public abstract class ValueGetProvider<T, V> implements ValueProvider<T, V> {
    @Override
    public final void setValue(T object, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String getPath() {
        throw new UnsupportedOperationException();
    }
}
