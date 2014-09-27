/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.shared.loader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseFilterConfigBuilder;
import com.ifree.common.gwt.client.ui.lists.FilterConfigBuilder;
import com.ifree.common.gwt.shared.SortDir;
import com.ifree.common.gwt.shared.SortInfoBean;

import java.util.List;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.07.13
 */
public class FilterPagingLoader<T, F extends Filter> extends PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>>
        implements ColumnSortEvent.Handler {


    private FilterConfigBuilder<F> filterConfigBuilder;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public FilterPagingLoader(DataProxy<FilterPagingLoadConfig, PagingLoadResult<T>> dataProxy) {
        super(dataProxy);
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected FilterPagingLoadConfig newLoadConfig() {
        return new FilterPagingLoadConfigBean();
    }

    @Override
    public void onColumnSort(ColumnSortEvent event) {

        ColumnSortList columnSortList = event.getColumnSortList();
        List<SortInfoBean> sortInfoBeans = toSortingBeanList(columnSortList);

        clearSortInfo();

        for (SortInfoBean sortInfoBean : sortInfoBeans) {
            addSortInfo(sortInfoBean);
        }

        load();
    }

    @Override
    protected FilterPagingLoadConfig prepareLoadConfig(FilterPagingLoadConfig config) {
        FilterPagingLoadConfig loadConfig = super.prepareLoadConfig(config);

        if (filterConfigBuilder != null) {
            loadConfig.setFilters(filterConfigBuilder.build());
        }

        return loadConfig;
    }

    private List<SortInfoBean> toSortingBeanList(ColumnSortList sorting) {

        List<SortInfoBean> list = Lists.newArrayList();
        for (int i = 0; i < sorting.size(); i++) {
            ColumnSortList.ColumnSortInfo columnSortInfo = sorting.get(i);
            String field = columnSortInfo.getColumn().getDataStoreName();

            Preconditions.checkNotNull(field);

            SortInfoBean sortInfoBean = new SortInfoBean(field, columnSortInfo.isAscending() ? SortDir.ASC : SortDir.DESC);
            list.add(sortInfoBean);
        }

        return list;
    }

    public void setFilter(F filter) {
        if (!GWT.isProdMode()) {
            Preconditions.checkNotNull(filterConfigBuilder);
        }
        filterConfigBuilder.setFilter(filter);
    }

    public void setConfigBuilder(BaseFilterConfigBuilder<F> configBuilder) {
        filterConfigBuilder = configBuilder;
    }
}
