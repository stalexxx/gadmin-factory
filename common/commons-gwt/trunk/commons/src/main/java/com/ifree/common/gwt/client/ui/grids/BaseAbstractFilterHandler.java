package com.ifree.common.gwt.client.ui.grids;

import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;

import java.util.List;

/**
 * Created by alex on 10.06.14.
 */
public final class BaseAbstractFilterHandler extends AbstractFilterHandler<BaseFilter> {

    public BaseAbstractFilterHandler(ValueProvider property, BaseFilterHelper helper) {
        super(property);
    }

    @Override
    protected BaseFilter createObj() {
        return new BaseFilter();
    }



}
