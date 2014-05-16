package com.ifree.common.gwt.client.ui.lists;

import com.ifree.common.gwt.client.ui.BaseToolbar;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.PageHeader;

/**
* Created by alex on 14.05.14.
*/
public class BaseViewPanel<T> extends FlowPanel {
    public BaseViewPanel(BaseListGrid<T> dataGrid, BaseToolbar toolbar, PageHeader header) {
        add(toolbar);
        add(header);
        add(dataGrid);
    }
}
