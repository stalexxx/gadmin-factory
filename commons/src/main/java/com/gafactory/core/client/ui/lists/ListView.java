/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.lists;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.gwtplatform.mvp.client.View;
import com.gafactory.core.client.actions.Action;
import com.gafactory.core.client.ui.application.CustomizedWithRoles;
import com.gafactory.core.client.ui.application.Filter;
import com.gafactory.core.client.ui.grids.BaseListGrid;
import com.gafactory.core.shared.loader.LoadHandler;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 *
 */
public interface ListView<M, F extends Filter> extends View, CustomizedWithRoles, ProvidesKey<M> {

    /**
     * Нельзя использовать чтобы получить конкретный объект, только чтобы отдать в ProvidesKey
     * @return
     */
    M getSelectedObject();

    boolean isSelected(M item);

    HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler);

    HandlerRegistration addLoadHandler(LoadHandler handler);

    void setSelection(M newSelection);

    void updateToolbar();

    void firstPage();

    void updateHeader(String displayHeader);

    void setFilter(F filter);

    void clearFilter();

    <W extends HasClickHandlers> W addAction(Action<M> action);

    void updateAction(Action<M> action, boolean enabled, boolean visible, String displayText, IconType displayIcon);

    /**
     * first input symbol
     * @param symbol
     */
    void focusFilter(Character symbol);

    ColumnSortList getColumnSortList();

    BaseListGrid<M, F> getGrid();

    void postConstruct();

    void scrollToSelected();
}
