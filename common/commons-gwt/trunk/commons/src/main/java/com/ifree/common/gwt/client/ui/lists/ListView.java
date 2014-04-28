/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.gwtplatform.mvp.client.View;
import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.ui.application.CustomizedWithRoles;
import com.ifree.common.gwt.client.ui.application.Filter;

/**
 *
 */
public interface ListView<M, F extends Filter> extends View, CustomizedWithRoles, ProvidesKey<M> {

    M getSelectedObject();

    boolean isSelected(M item);

    HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);

    HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler);


    HasData<M> getGridDataDisplay();

    void setSelection(M newSelection);

    void displayFilter(F filter);

    void updateToolbar();

    void firstPage();

    void updateHeader(String displayHeader);

    void setFilter(F filter);

    void addAction(Action<M> action, Command command);

    void updateAction(Action<M> action, boolean enabled, boolean visible, String displayText);

}
