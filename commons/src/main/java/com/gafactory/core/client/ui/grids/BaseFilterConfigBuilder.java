/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.grids;

import com.google.common.collect.Lists;
import com.gafactory.core.client.ui.application.Filter;
import com.gafactory.core.client.ui.lists.FilterConfigBuilder;
import com.gafactory.core.shared.loader.*;

import java.util.List;

/**
* @author Alexander Ostrovskiy (a.ostrovskiy)
* @since 24.09.13
*/
public class BaseFilterConfigBuilder<F extends Filter> implements FilterConfigBuilder<F> {

    protected F filter;
    private CustomFilterAppender<F> appender;


    public BaseFilterConfigBuilder(CustomFilterAppender<F> appender) {
        this.appender = appender;
    }


    @Override
    public void setFilter(F filter) {
        this.filter = filter;
    }

    @Override
    public List<FilterConfigBean> build() {
        final List<FilterConfigBean> filterConfigs = Lists.newArrayList();

        if (filter != null) {
            appender.addCustomFields(filter, filterConfigs);
        }

        return filterConfigs;

    }

    public interface CustomFilterAppender<F> {
        void addCustomFields(F filter, List<FilterConfigBean> filterConfigs);
    }

}