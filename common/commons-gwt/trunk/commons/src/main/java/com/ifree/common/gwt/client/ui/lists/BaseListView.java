/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import com.ifree.common.gwt.client.ui.grids.BasePager;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.PageHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Ostrovskiy (alex)
 * @since 03.09.13
 */
public abstract class BaseListView<
        T,
        _Filter extends BaseFilter,
        _Handler extends ListUiHandler<T, _Filter>
        >
        extends ViewWithUiHandlers<_Handler> implements ListView<T, _Filter> {

    protected final PageHeader header;
    protected final BaseListGrid<T> dataGrid;
    protected final BaseFilterPanel<_Filter, ? extends BaseFilterPanel> filterPanel;
    protected final BaseToolbar toolbar;

    private UIActionBuilder<T, AnchorListItem> actionBuilder = new ListItemActionBuilder<T>();
    private Map<Action<T>, AnchorListItem> actionMap = Maps.newHashMap();

    protected BaseListView(BaseListGrid<T> dataGrid) {
        this(dataGrid, null);
    }

    protected BaseListView(@Nonnull BaseListGrid<T> grid,
                           @Nullable BaseFilterPanel<_Filter, ? extends BaseFilterPanel> filterPanel) {
        Preconditions.checkNotNull(grid);

        dataGrid = grid;
        dataGrid.addStyleName("gridAfterFixedBar");
        this.filterPanel = filterPanel;

        toolbar = new BaseToolbar();
        toolbar.setExtendSearch(false);

        if (filterPanel != null) {
            toolbar.add(filterPanel);
        }

        header = new PageHeader();

        toolbar.addPerformFilterHandler(new PerformFilterEvent.PerformFilterHandler() {
            @Override
            public void onPerformFilter(PerformFilterEvent event) {
                getUiHandlers().onPerformFilter(event);
            }
        });

        BaseViewPanel<T> viewPanel = new BaseViewPanel<T>(dataGrid, toolbar, header);
        viewPanel.addPager(dataGrid.getPager());

        initWidget(viewPanel);
    }


    @Override
    public void setUiHandlers(_Handler uiHandlers) {
        super.setUiHandlers(uiHandlers);
        if (filterPanel != null) {
            this.filterPanel.setUiHandlers(getUiHandlers());
        }

    }

    @Override
    public T getSelectedObject() {
        return dataGrid.getSelection();
    }

    @Override
    public boolean isSelected(T item) {
        return dataGrid.isSelected(item);
    }


    @Override
    public void setupRoles(List<String> roles) {

    }

    @Override
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
        return dataGrid.addColumnSortHandler(handler);
    }

    @Override
    public HasData<T> getGridDataDisplay() {
        return dataGrid.getDisplay();
    }


    @Override
    public Object getKey(T item) {
        return dataGrid.getKey(item);
    }

    @Override
    public void setSelection(T newSelection) {
        dataGrid.setSelection(newSelection);
    }

    @Override
    public final HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return dataGrid.addSelectionChangeHandler(handler);
    }

    @Override
    public void updateToolbar() {
        final int offsetHeight = toolbar.getOffsetHeight();
        dataGrid.getElement().getStyle().setTop(offsetHeight, Style.Unit.PX);
    }

    @Override
    public void firstPage() {
        BasePager pager = dataGrid.getPager();
        if (pager != null) {
            pager.firstPage();
        }
    }

    @Override
    public void updateHeader(String displayHeader) {
        header.setText(displayHeader);
    }

    @Override
    public void setFilter(_Filter filter) {
        TakesValue<_Filter> filterPeer = filterPanel;
        if (filterPeer != null) {
            filterPeer.setValue(filter);
        }
    }

    @Override
    public void addAction(Action<T> action, final Command command) {
        AnchorListItem actionWidget = actionBuilder.build(action);
        actionWidget.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                command.execute();
            }
        });
        actionMap.put(action, actionWidget);
        toolbar.addAction(action, actionWidget);
    }

    @Override
    public void updateAction(Action<T> action, boolean enabled, boolean visible, String displayText) {
        AnchorListItem listItem = actionMap.get(action);
        if (listItem != null) {
            listItem.setEnabled(enabled);
            listItem.setText(displayText);
            listItem.setVisible(visible);
        }
    }

    @Override
    public void focusFilter(Character symbol) {
        toolbar.focusSearchBySymbol(symbol);
    }

    @Override
    public ColumnSortList getColumnSortList() {
        return dataGrid.getColumnSortList();
    }

}
