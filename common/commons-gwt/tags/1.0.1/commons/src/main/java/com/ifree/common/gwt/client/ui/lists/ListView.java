/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.gwtplatform.mvp.client.View;
import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.ui.application.CustomizedWithRoles;
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.grids.BaseListGrid;
import com.ifree.common.gwt.shared.loader.LoadEvent;
import com.ifree.common.gwt.shared.loader.LoadHandler;

/**
 *
 */
public interface ListView<M, F extends Filter> extends View, CustomizedWithRoles, ProvidesKey<M> {

    M getSelectedObject();

    boolean isSelected(M item);

    HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler);

    HandlerRegistration addLoadHandler(LoadHandler handler);

    void setSelection(M newSelection);

    void updateToolbar();

    void firstPage();

    void updateHeader(String displayHeader);

    void setFilter(F filter);

    void addAction(Action<M> action, Command command);

    void updateAction(Action<M> action, boolean enabled, boolean visible, String displayText);

    /**
     * first input symbol
     * @param symbol
     */
    void focusFilter(Character symbol);

    ColumnSortList getColumnSortList();

    BaseListGrid<M, F> getGrid();
}
