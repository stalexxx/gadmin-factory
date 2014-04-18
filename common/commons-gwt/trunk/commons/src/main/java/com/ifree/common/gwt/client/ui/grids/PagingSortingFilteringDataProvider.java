/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.collect.Lists;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.SortInfoBean;
import com.ifree.common.gwt.shared.SortDir;
import com.ifree.common.gwt.shared.SortInfo;
import com.ifree.common.gwt.shared.loader.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * paging
 * filtering
 * sorting
 *
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 08.07.13
 */
public class PagingSortingFilteringDataProvider<T, F extends BaseFilter> extends AsyncDataProvider<T>
        implements LoadHandler<FilterPagingLoadConfig, PagingLoadResult<T>>,
        BeforeLoadEvent.BeforeLoadHandler<FilterPagingLoadConfig> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader;
    private ProvidesKey<T> providesKey;
    
    private List<SortInfo> sortInfoList = Lists.newArrayList();
    private List<T> currentData;

    private final FilterConfigBuilder<F> filterConfigBuilder;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public PagingSortingFilteringDataProvider(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader, ProvidesKey<T> providesKey, FilterConfigBuilder<F> fcb) {
        this.loader = loader;
        this.providesKey = providesKey;

        loader.addLoadHandler(this);
        loader.addBeforeLoadHandler(this);

        filterConfigBuilder = fcb;


    }

    public PagingSortingFilteringDataProvider(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader, ProvidesKey<T> providesKey) {
        this(loader, providesKey, new BaseFilterConfigBuilder<F>());
    }

    /*===========================================[ CLASS METHODS ]================*/

    public void pushSortingInfo(SortInfo sortInfo) {
        if (sortInfoList == null) {
            sortInfoList = Lists.newArrayList();
        }

        sortInfoList.add(new SortInfoBean(sortInfo.getSortField(), sortInfo.getSortDir()));
    }

    public void pushSortingInfo(String field, SortDir sortDir) {
        pushSortingInfo(new SortInfoBean(field, sortDir));
    }
    public void setSorting(ColumnSortList sorting) {

        List<SortInfo> list = Lists.newArrayList();

        for (int i = 0; i < sorting.size(); i++) {
            ColumnSortList.ColumnSortInfo columnSortInfo = sorting.get(i);

            String field = columnSortInfo.getColumn().getDataStoreName();

            list.add(new SortInfoBean(field, columnSortInfo.isAscending() ? SortDir.ASC : SortDir.DESC));
        }

        this.sortInfoList = list;
    }


    public void updateModel(T model) {
        if (currentData != null) {
            int i = currentData.indexOf(model);
            if (i != -1) {
                updateRowData(i, Lists.newArrayList(model));
            }
        }

    }

    @Nullable
    public T findModel(Object key) {
        if (currentData != null) {
            for (T t : currentData) {
                if (providesKey.getKey(t).equals(key)) {
                    return t;
                }
            }
        }

        return null;
    }
    public void setFilter(F filter) {
        filterConfigBuilder.setFilter(filter);        
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<T>> event) {
        PagingLoadResult<T> loadResult = event.getLoadResult();


        if (loadResult instanceof PagingLoadResultBean) {
            PagingLoadResultBean result = (PagingLoadResultBean) loadResult;
            currentData = result.getData();

            updateRowCount(result.getTotalLength(), true);
            updateRowData(result.getOffset(), currentData);
        }
    }
    
    @Override
    public void onBeforeLoad(BeforeLoadEvent<FilterPagingLoadConfig> event) {
        FilterPagingLoadConfig loadConfig = event.getLoadConfig();
                
        loadConfig.setFilters(filterConfigBuilder.build());

        loadConfig.setSortInfo(sortInfoList);

    }

    @Override
    protected void onRangeChanged(HasData display) {
        Range visibleRange = display.getVisibleRange();

        loader.load(visibleRange.getStart(), visibleRange.getLength());
    }

    /*===========================================[ INNER CLASSES ]================*/

    public interface FilterConfigBuilder<F extends Filter> {

        void setFilter(F filter);
        
        List<FilterConfigBean> build();
    }
    
    public static class DefaultFilterConfigBuilder implements FilterConfigBuilder<BaseFilter> {

        private BaseFilter filter;

        @Override
        public void setFilter(BaseFilter filter) {
            this.filter = filter;
        }

        @Override
        public List<FilterConfigBean> build() {
            final List<FilterConfigBean> filterConfigs = Lists.newArrayList();

            if (filter != null) {
                final String nameFilter = filter.getName();
                if (nameFilter != null && !nameFilter.isEmpty()) {
                    FilterConfigBean fc = new FilterConfigBean();
                    fc.setValue(nameFilter);

                    filterConfigs.add(fc);
                }
            }

            return filterConfigs;
        }
    }

}
