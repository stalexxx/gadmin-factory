/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.collect.Lists;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.*;

import java.util.List;

/**
* @author Alexander Ostrovskiy (a.ostrovskiy)
* @since 24.09.13
*/
public class BaseFilterConfigBuilder<F extends BaseFilter> implements PagingSortingFilteringDataProvider.FilterConfigBuilder<F> {

    protected F filter;

    private static FilterHelper helper = new FilterHelper();

    private ValueProvider<?, ?> defaultField;

    public BaseFilterConfigBuilder(ValueProvider<?, ?> defaultField) {
        this.defaultField = defaultField;
    }

    public BaseFilterConfigBuilder() {

    }


    @Override
    public void setFilter(F filter) {
        this.filter = filter;
    }

    @Override
    public List<FilterConfigBean> build() {
        final List<FilterConfigBean> filterConfigs = Lists.newArrayList();

        if (filter != null) {
            addCustomFields(filterConfigs);
        }

        return filterConfigs;

    }


    protected void addCustomFields(List<FilterConfigBean> filterConfigs) {
        if (defaultField != null) {
            helper.appendTo(filterConfigs, defaultField, filter.getName());
        }
    }

    protected  static <V> void addField(List<FilterConfigBean> filterConfigs, ValueProvider<?, ?> field, V value) {
        helper.appendTo(filterConfigs, field, value);
    }



}