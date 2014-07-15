package com.ifree.common.gwt.client.ui.grids;

import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.ValueGetProvider;
import com.ifree.common.gwt.shared.ValueProvider;

/**
 * Created by alex on 10.06.14.
 */
public final class BaseAbstractFilterHandler extends AbstractFilterHandler<BaseFilter> {

    public BaseAbstractFilterHandler(ValueProvider property, BaseFilterHelper helper) {
        super(new BaseFilterProvider(property.getPath()));
        filterHelper = helper;
    }

    @Override
    protected BaseFilter createObj() {
        return new BaseFilter();
    }


    public static class BaseFilterProvider extends ValueGetProvider<BaseFilter, String> {
        public BaseFilterProvider(String path) {
            super(path);
        }

        @Override
        public String getValue(BaseFilter object) {
            return object.getName();
        }
    }
}
