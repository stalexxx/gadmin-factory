/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.inject.Provider;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.SortInfoBean;
import com.ifree.common.gwt.shared.SortDir;
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
    private Provider<FilterConfigBuilder<F>> fcbProvider;

   // private List<SortInfoBean> sortInfoList = Lists.newArrayList();
    private List<T> currentData;

    private FilterConfigBuilder<F> filterConfigBuilder;
    private Provider<ColumnSortList> sortListProvider;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public PagingSortingFilteringDataProvider(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader, ProvidesKey<T> providesKey, Provider<FilterConfigBuilder<F>> fcb) {
        this.loader = loader;
        this.providesKey = providesKey;

        fcbProvider = fcb;

        loader.addLoadHandler(this);
        loader.addBeforeLoadHandler(this);

       // filterConfigBuilder = fcb;


    }

    private FilterConfigBuilder<F> getFilterConfigBuilder() {
        if (filterConfigBuilder == null) {
            filterConfigBuilder = fcbProvider.get();
        }

        return filterConfigBuilder;
    }

    /*===========================================[ CLASS METHODS ]================*/

    private List<SortInfoBean> toSortingBeanList(ColumnSortList sorting) {

        List<SortInfoBean> list = Lists.newArrayList();

        for (int i = 0; i < sorting.size(); i++) {
            ColumnSortList.ColumnSortInfo columnSortInfo = sorting.get(i);

            String field = columnSortInfo.getColumn().getDataStoreName();

            Preconditions.checkNotNull(field);

            list.add(new SortInfoBean(field, columnSortInfo.isAscending() ? SortDir.ASC : SortDir.DESC));
        }

        return list;
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
        getFilterConfigBuilder().setFilter(filter);
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

        loadConfig.setFilters(getFilterConfigBuilder().build());

        loadConfig.setSortInfo(toSortingBeanList(sortListProvider.get()));

    }

    @Override
    protected void onRangeChanged(HasData display) {
        Range visibleRange = display.getVisibleRange();

        loader.load(visibleRange.getStart(), visibleRange.getLength());
    }

    public void setSortListProvider(Provider<ColumnSortList> filterProvider) {
        this.sortListProvider = filterProvider;
    }

    /*===========================================[ INNER CLASSES ]================*/

    public interface FilterConfigBuilder<F extends Filter> {

        void setFilter(F filter);
        
        List<FilterConfigBean> build();



    }
    


}
