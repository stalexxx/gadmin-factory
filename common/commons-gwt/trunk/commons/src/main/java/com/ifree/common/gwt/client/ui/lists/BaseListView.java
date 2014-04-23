/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.HasData;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.PageHeader;

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


    @UiField
    public PageHeader header;

    private UIActionBuilder<T, ListItem> actionBuilder = new ListItemActionBuilder<T>();
    private Map<Action<T>, ListItem> actionMap = Maps.newHashMap();


    protected BaseListView() {

    }

    protected abstract BaseToolbar getToolbar();

    @Override
    public T getSelectedObject() {
        return getDataGrid().getSelection();
    }

    @Override
    public boolean isSelected(T item) {
        return getDataGrid().isSelected(item);
    }


    @Override
    public void setupRoles(List<String> roles) {

    }

    @Override
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
        return getDataGrid().addColumnSortHandler(handler);
    }

    @Override
    public HasData<T> getGridDataDisplay() {
        return getDataGrid().getDisplay();
    }

    public abstract BaseListGrid<T> getDataGrid();


    @Override
    public Object getKey(T item) {
        return getDataGrid().getKey(item);
    }

    @Override
    public void setSelection(T newSelection) {
        getDataGrid().setSelection(newSelection);
    }

    @Override
    public void displayFilter(_Filter filter) {

    }

    @Override
    public void updateToolbar() {
        final int offsetHeight = getToolbar().getOffsetHeight();
        getDataGrid().getElement().getStyle().setTop(offsetHeight, Style.Unit.PX);
    }

    @Override
    public void firstPage() {
        SimplePager pager = getDataGrid().getPager();
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
        TakesValue<_Filter> filterPeer = getFilterPeer();
        if (filterPeer != null) {
            filterPeer.setValue(filter);
        }
    }

    protected TakesValue<_Filter> getFilterPeer() {
        return null;
    }

    @Override
    public void addAction(Action<T> action, final Command command) {
        ListItem actionWidget = actionBuilder.build(action);
        actionWidget.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                command.execute();
            }
        });
        actionMap.put(action, actionWidget);
        getToolbar().add(actionWidget);
    }

    @Override
    public void updateAction(Action<T> action, boolean enabled) {
        ListItem listItem = actionMap.get(action);
        if (listItem != null) {
            listItem.setEnabled(enabled);
        }
    }

    protected  _Filter castFilter(BaseFilter filter) {
        return (_Filter) filter;
    }
}
