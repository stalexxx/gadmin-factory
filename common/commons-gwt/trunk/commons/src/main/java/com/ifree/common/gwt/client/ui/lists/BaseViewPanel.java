package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Widget;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

/**
* Created by alex on 14.05.14.
*/
public class BaseViewPanel<T, F extends Filter> extends FlowPanel {

    private final Row row;
    public BaseViewPanel(BaseListGrid<T, F> dataGrid, BaseToolbar toolbar) {
        add(toolbar);
        add(dataGrid);

        Container footer = new Container();
        footer.setFluid(true);

        add(footer);

        footer.add(row = new Row());
    }

    public void addPager(AbstractPager pager) {
        if (pager != null) {
            row.add(wrap(pager, ColumnSize.MD_11));
        }
    }

    public void addPageSizeWidget(Widget pageSizeWidget) {
        if (pageSizeWidget != null) {
            pageSizeWidget.addStyleName("pagination");
            row.add(wrap(pageSizeWidget, ColumnSize.MD_1));

        }
    }

    private Column wrap(Widget widget, ColumnSize md8) {
        Column pagerCol = new Column(md8);
        pagerCol.add(widget);

        return pagerCol;
    }
}
