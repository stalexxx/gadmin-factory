/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.view.client.HasData;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import com.ifree.common.gwt.client.ui.BaseToolbar;
import org.gwtbootstrap3.client.ui.PageHeader;

import java.util.List;

/**
 * @author Alexander Ostrovskiy (alex)
 * @since 03.09.13
 */
public abstract class BaseListView<
        T,
        _Filter extends Filter,
        _Handler extends ListUiHandler<T, _Filter>
        >
        extends ViewWithUiHandlers<_Handler> implements ListView<T, _Filter> {


    @UiField
    public PageHeader header;

    protected TakesValue<_Filter> filterPanel;



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
    public void updateControls(T selectedObject) {
        if (getCreateControl() != null) {
            getCreateControl().setEnabled(true);
        }
        if (getEditControl() != null) {
            getEditControl().setEnabled(selectedObject != null);
        }
        if (getRemoveControl() != null) {
            getRemoveControl().setEnabled(selectedObject != null);
        }
        if (getViewControl() != null) {
            getViewControl().setEnabled(selectedObject != null);
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

    protected abstract TakesValue<_Filter> getFilterPeer();


    protected HasEnabled getViewControl() {
        return null;

    }

    protected HasEnabled getRemoveControl() {
        return null;

    }

    protected HasEnabled getEditControl() {
        return null;

    }

    protected HasEnabled getCreateControl() {
        return null;

    }

    protected  _Filter castFilter(BaseFilter filter) {
        return (_Filter) filter;
    }
}
