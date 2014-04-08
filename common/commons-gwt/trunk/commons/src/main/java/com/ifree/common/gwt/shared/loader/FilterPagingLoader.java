/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.shared.loader;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.07.13
 */
public class FilterPagingLoader<T> extends PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> {

    /*===========================================[ CONSTRUCTORS ]=================*/

    public FilterPagingLoader(DataProxy<FilterPagingLoadConfig, PagingLoadResult<T>> dataProxy) {
        super(dataProxy);
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected FilterPagingLoadConfig newLoadConfig() {
        return new FilterPagingLoadConfigBean();
    }
}
