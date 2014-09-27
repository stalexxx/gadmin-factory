package com.ifree.common.gwt.client.data;

import com.google.common.base.Joiner;
import com.google.gwt.storage.client.Storage;
import com.ifree.common.gwt.client.ui.application.security.CurrentUser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Created by alex on 05.08.14.
 */
public class StorageService {

    @Inject
    private CurrentUser currentUser;

    private Storage storage;


    public StorageService() {
        this.storage = Storage.getLocalStorageIfSupported();
    }

    private String getKey(String view, String property) {
        return Joiner.on(".").join(currentUser.getUserName(), view, property);
    }

    public <T> T getValue(Property<T> property, String view) {
        String key = getKey(view, property.propertyName());
        String item = storage.getItem(key);
        return property.convertFromString(item);
    }

    public <T> void putValue(Property<T> property, T value, String view) {
        String key = getKey(view, property.propertyName());

        storage.setItem(key, property.convertToString(value));
    }

    public static abstract class Property<T> {

        private final String name;

        protected Property(String name) {
            this.name = name;
        }

        public String propertyName() {
            return name;
        }

        public T convertFromString(String value) {
            if (value != null) {
                return parseNonNull(value);
            }
            return null;
        }

        public abstract String convertToString(@Nullable T obj);

        protected abstract T parseNonNull(@Nonnull String value);

    }
}
