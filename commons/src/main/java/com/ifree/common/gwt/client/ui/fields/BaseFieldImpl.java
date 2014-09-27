package com.ifree.common.gwt.client.ui.fields;

/**
 * Created by alex on 21.04.14.
 */
public abstract class BaseFieldImpl<T, V> implements BaseField<T, V> {
    private String path;
    private boolean defaultField;

    public BaseFieldImpl(String name, boolean def) {
        this.path = name;
        this.defaultField = def;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isDefaultField() {
        return defaultField;
    }
}
