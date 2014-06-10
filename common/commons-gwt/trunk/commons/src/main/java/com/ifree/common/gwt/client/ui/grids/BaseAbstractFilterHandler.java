package com.ifree.common.gwt.client.ui.grids;

import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;

import java.util.List;

/**
 * Created by alex on 10.06.14.
 */
public final class BaseAbstractFilterHandler extends AbstractFilterHandler<BaseFilter> {
    private final ValueProvider property;
    private BaseFilterHelper helper;

    public BaseAbstractFilterHandler(ValueProvider property, BaseFilterHelper helper) {
        this.property = property;
        this.helper = helper;
    }

    @Override
    protected BaseFilter createObj() {
        return new BaseFilter();
    }

    @Override
    public void addCustomFields(BaseFilter filter, List<FilterConfigBean> filterConfigs) {
        helper.appendTo(filterConfigs, property, filter.getName());
    }

}
