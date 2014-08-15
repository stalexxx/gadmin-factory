package com.ifree.common.gwt.shared;

/**
 * Created by alex on 25.06.14.
 */
public abstract class ValueGetProvider<T, V> implements ValueProvider<T, V> {
    private String path;

    public ValueGetProvider(String path) {

        this.path = path;
    }

    public ValueGetProvider() {
    }

    @Override
    public void setValue(T object, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String getPath() {
        if (path != null) {
            return path;
        }
        throw new UnsupportedOperationException();
    }
}
