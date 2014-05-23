package com.ifree.common.gwt.client.utils;

import com.google.gwt.text.shared.Renderer;

/**
 * Created by alex on 19.05.14.
 */
public interface RendererResolver {
    <T> Renderer<T> get(Class<T> key);

    <T> Renderer<T> get(Class<T> key, String defaultValue);

    <T> Renderer<T> getNotNull(Class<T> key);

    <T> void register(Class<T> key, Renderer<T> value);

}
