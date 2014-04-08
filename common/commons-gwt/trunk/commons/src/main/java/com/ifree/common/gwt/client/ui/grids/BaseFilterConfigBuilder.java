/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.collect.Lists;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.loader.FilterConfig;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;

import java.util.List;

/**
* @author Alexander Ostrovskiy (a.ostrovskiy)
* @since 24.09.13
*/
public class BaseFilterConfigBuilder<F extends BaseFilter> implements PagingSortingFilteringDataProvider.FilterConfigBuilder<F> {

    protected F filter;

    @Override
    public void setFilter(F filter) {

        this.filter = filter;
    }

    @Override
    public List<FilterConfig> build() {
        final List<FilterConfig> filterConfigs = Lists.newArrayList();

        if (filter != null) {
            final String nameFilter = filter.getNameFilter();
            addField(filterConfigs, nameFilter, "name");
        }

        return filterConfigs;

    }

    protected static void addField(List<FilterConfig> filterConfigs, String value, String field) {
        if (value != null && !value.isEmpty()) {
            FilterConfig fc = new FilterConfigBean();
            fc.setValue(value);
            fc.setField(field);

            filterConfigs.add(fc);
        }
    }

    protected static void addField(List<FilterConfig> filterConfigs, String value, Enum field) {
        addField(filterConfigs, value, field.name());

    }
}