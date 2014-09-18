package com.ifree.common.gwt.client.ui.lists;

import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;

import java.util.List;

public interface FilterConfigBuilder<F extends Filter> {

        void setFilter(F filter);
        
        List<FilterConfigBean> build();

    }