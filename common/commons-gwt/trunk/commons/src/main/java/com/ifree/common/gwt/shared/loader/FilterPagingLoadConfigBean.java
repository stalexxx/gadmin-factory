/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;


import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PagingLoadConfigBean} with support for filters.
 */
public class FilterPagingLoadConfigBean extends PagingLoadConfigBean implements FilterPagingLoadConfig {

    private List<FilterConfigBean> filterConfigs = new ArrayList<FilterConfigBean>();

    @Override
    public List<FilterConfigBean> getFilters() {
        return filterConfigs;
    }

    @Override
    public void setFilters(List<FilterConfigBean> filters) {
        this.filterConfigs = filters;
    }

    public void addFilter(FilterConfigBean filterConfigBean) {
        filterConfigs.add(filterConfigBean);
    }


}
