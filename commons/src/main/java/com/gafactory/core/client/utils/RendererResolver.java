package com.gafactory.core.client.utils;

import com.google.gwt.text.shared.Renderer;

/**
 * Created by alex on 19.05.14.
 */
public interface RendererResolver {
    <T> Renderer<T> get(Class<T> key);

    <T> Renderer<T> getNotNull(Class<T> key);

    <T> Renderer<T> getNotNull(Class<T> key, String nullDisplay);

    <T> void register(Class<T> key, Renderer<T> value);

}
