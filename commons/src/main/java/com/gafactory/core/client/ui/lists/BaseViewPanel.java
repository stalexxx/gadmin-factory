package com.gafactory.core.client.ui.lists;

import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Widget;
import com.gafactory.core.client.ui.BaseFilter;
import com.gafactory.core.client.ui.BaseToolbar;
import com.gafactory.core.client.ui.grids.BaseListGrid;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.NavbarType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

/**
* Created by alex on 14.05.14.
*/
public class BaseViewPanel<T, F extends BaseFilter> extends FlowPanel {

    private final Row footerRow;
    private final Row mainRow;

    private final Column gridColumn;


    public BaseViewPanel(BaseListGrid<T, F> dataGrid, BaseToolbar toolbar, BaseFilterPanel<F, ? extends BaseFilterPanel> filterPanel) {

        Container container = new Container();
        container.setFluid(true);
        container.add(mainRow = new Row());

        add(toolbar);
        add(container);

        gridColumn = new Column(ColumnSize.LG_10, ColumnSize.MD_10, ColumnSize.SM_12);
        gridColumn.add(dataGrid);
        mainRow.add(gridColumn);

        if (filterPanel != null) {
            Column filterPanelContainer = new Column(ColumnSize.LG_2,ColumnSize.MD_2, ColumnSize.SM_6);

            filterPanelContainer.addStyleName(NavbarType.DEFAULT.getCssName());
            filterPanelContainer.add(filterPanel);
            mainRow.add(filterPanelContainer);

        }

        footerRow = new Row();
        gridColumn.add(footerRow);

    }

    public void addPager(AbstractPager pager) {
        if (pager != null) {
            footerRow.add(wrap(pager, ColumnSize.MD_10, ColumnSize.SM_9, ColumnSize.LG_11, ColumnSize.XS_9));
        }
    }

    public void addPageSizeWidget(Widget pageSizeWidget) {
        if (pageSizeWidget != null) {
            pageSizeWidget.addStyleName("pagination");
            footerRow.add(wrap(pageSizeWidget, ColumnSize.MD_2, ColumnSize.SM_3, ColumnSize.LG_1, ColumnSize.XS_3));
        }
    }

    private Column wrap(Widget widget, ColumnSize md8, ColumnSize ... elseSize) {
        Column pagerCol = new Column(md8, elseSize);
        pagerCol.add(widget);

        return pagerCol;
    }
}
