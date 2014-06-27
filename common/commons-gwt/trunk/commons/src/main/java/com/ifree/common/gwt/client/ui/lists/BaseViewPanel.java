package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.PageHeader;

/**
* Created by alex on 14.05.14.
*/
public class BaseViewPanel<T, F extends Filter> extends FlowPanel {
    public BaseViewPanel(BaseListGrid<T, F > dataGrid, BaseToolbar toolbar, PageHeader header) {
        add(toolbar);
        add(header);
        add(dataGrid);
    }

    public void addPager(AbstractPager pager) {
        if (pager != null) {
            add(pager);
        }
    }
}
