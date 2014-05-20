/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.ifree.common.gwt.shared.SortInfoBean;
import com.ifree.common.gwt.shared.loader.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 23.04.13
 */
public abstract class BaseDataProxy<T>
        implements DataProxy<FilterPagingLoadConfig, PagingLoadResult<T>> {

    public static final int DELAY_MILLIS = 300;

    @Inject
    protected RestDispatch restDispatch;

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
                if (filter.getField() == field.name()) {
                    return filter.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig, final Callback<PagingLoadResult<T>, Throwable> callback) {

        this.loadConfig = loadConfig;
        this.callback = callback;

        timer.cancel();
        timer.schedule(DELAY_MILLIS);
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

        restDispatch.execute(action, new AsyncCallback<PagingLoadResultBean<T>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(PagingLoadResultBean<T> result) {
                callback.onSuccess(result);
            }
        });
    }

    private String filter;

    private SortInfoBean defaultSortInfo;


    public void setDefaultSortInfo(SortInfoBean defaultSortInfo) {
        this.defaultSortInfo = defaultSortInfo;
    }

/*
    @NotNull
    protected List<SortInfo> getSortInfos(PagingLoadConfig loadConfig, RequestContext request) {
        List<? extends SortInfo> sortInfos = loadConfig.getSortInfo();

        if (sortInfos != null && !sortInfos.isEmpty()) {
            ArrayList<SortInfo> target = new ArrayList<SortInfo>(sortInfos.size());

            for (SortInfo info : sortInfos) {

                target.add(getSortInfo(request, info));
            }

            return target;
        }

        if (defaultSortInfo != null) {
            return Lists.newArrayList(getSortInfo(request, defaultSortInfo));
        }

        return Lists.newArrayList();
    }
*/

    /*private static SortInfo getSortInfo(RequestContext request, SortInfo info) {
        SortInfo sortInfo = new SortInfoBean(info.getSortField(), info.getSortDir());
        return sortInfo;
    }*/

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
