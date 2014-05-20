package com.ifree.common.gwt.client.data;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.Callback;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.shared.loader.DataProxy;
import com.ifree.common.gwt.shared.loader.PagingLoadResult;

import java.util.Map;

/**
 * Created by alex on 05.05.14.
 */
public class CachingDataProxy implements DataProxy {
    private Map<Class, BaseDataProxy> dataProxyMap = Maps.newHashMap();

    public <T> void registerDataProxy(Class<T> clazz, BaseDataProxy<T> dataProxy) {
        dataProxyMap.put(clazz, dataProxy);
    }

    public <C, R extends PagingLoadResult> void load(Class<R> type, Callback<R, Throwable> callback) {
        BaseDataProxy<R> baseDataProxy = dataProxyMap.get(type);
//        baseDataProxy.instantLoad(null, callback);
    }

    @Override
    public void load(Object loadConfig, Callback callback) {

    }
}
