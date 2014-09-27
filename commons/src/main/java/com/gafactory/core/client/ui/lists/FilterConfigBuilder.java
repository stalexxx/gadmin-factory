package com.gafactory.core.client.ui.lists;

import com.gafactory.core.client.ui.application.Filter;
import com.gafactory.core.shared.loader.FilterConfigBean;

import java.util.List;

public interface FilterConfigBuilder<F extends Filter> {

        void setFilter(F filter);
        
        List<FilterConfigBean> build();

    }