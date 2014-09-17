/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.proxy.NotifyingAsyncCallback;
import com.ifree.common.gwt.shared.loader.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 23.04.13
 */
public abstract class BaseDataProxy<T>
        implements DataProxy<FilterPagingLoadConfig, PagingLoadResult<T>> {

    private static final int DELAY_MILLIS = 500;

    @Inject
    private EventBus eventBus;

    @Inject
    RestDispatch restDispatch;

    private FilterPagingLoadConfig loadConfig;

    private Callback<PagingLoadResult<T>, Throwable> callback;

    Timer timer = new Timer() {
        @Override
        public void run() {
            doLoad(loadConfig, callback);
        }
    };

    @Nullable
    protected static String findValueByKey(List<FilterConfig> filters, Enum field) {
        if (filters != null) {
            for (FilterConfig filter : filters) {
                if (Objects.equals(filter.getField(), field.name())) {
                    return filter.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig, final Callback<PagingLoadResult<T>, Throwable> callback) {

        this.loadConfig = prepareLoadConfig(loadConfig);
        this.callback = callback;

        timer.cancel();
        timer.schedule(DELAY_MILLIS);
    }

    FilterPagingLoadConfig prepareLoadConfig(FilterPagingLoadConfig loadConfig) {
        return loadConfig;
    }

    public void load(final Callback<PagingLoadResult<T>, Throwable> callback) {
        load(null, callback);
    }

    public void instantLoad(final AsyncCallback<PagingLoadResult<T>> callback) {
        doLoad(null, new Callback<PagingLoadResult<T>, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess(PagingLoadResult<T> result) {
                callback.onSuccess(result);
            }
        });
    }

    public void doLoad(FilterPagingLoadConfig loadConfig, final Callback<PagingLoadResult<T>, Throwable> callback) {
        final RestAction<PagingLoadResultBean<T>> action;
        if (loadConfig != null) {
            action = getAction((FilterPagingLoadConfigBean) loadConfig);
        } else {
            action = getAction();
        }

            NotifyingAsyncCallback<PagingLoadResultBean<T>> notifyingAsyncCallback = new NotifyingAsyncCallback<PagingLoadResultBean<T>>(eventBus) {

            @Override
            protected void success(PagingLoadResultBean<T> result) {
                callback.onSuccess(result);

            }
        };

        notifyingAsyncCallback.prepare();

        restDispatch.execute(action, notifyingAsyncCallback);

        notifyingAsyncCallback.checkLoading();
    }

    private String filter;

    protected String buildQuery(List<FilterConfig> filters) {
        if (filters != null && !filters.isEmpty()) {
            FilterConfig config = filters.iterator().next();
            return config.getValue();
        }
        return null;
    }

    public boolean hasFilter() {
        return filter != null && !filter.isEmpty();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    /*protected void onExecutionFailure(Throwable callback) {
        alertsPanel.addAlert(messages.cantLoadData(callback.getMessage()), AlertType.ERROR);
    }*/


    protected abstract RestAction<PagingLoadResultBean<T>> getAction(FilterPagingLoadConfigBean loadConfig);

    protected abstract RestAction<PagingLoadResultBean<T>> getAction();


}
