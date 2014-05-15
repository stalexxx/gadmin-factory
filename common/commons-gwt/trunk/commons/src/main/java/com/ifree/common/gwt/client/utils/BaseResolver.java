package com.ifree.common.gwt.client.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 16.04.14.
 */
public class BaseResolver<K, R> {
    protected Map<K, R> map = Maps.newHashMap();

    public R resolve(K key) {
        return map.get(key);
    }

    public void register(K key, R value) {
        map.put(key, value);
    }
}
