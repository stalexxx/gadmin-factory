package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.user.cellview.client.AbstractPager;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

/**
* Created by alex on 14.05.14.
*/
public class BaseViewPanel<T, F extends Filter> extends FlowPanel {
    public BaseViewPanel(BaseListGrid<T, F> dataGrid, BaseToolbar toolbar) {
        add(toolbar);
        add(dataGrid);
    }

    public void addPager(AbstractPager pager) {
        if (pager != null) {
            add(pager);
        }
    }
}
