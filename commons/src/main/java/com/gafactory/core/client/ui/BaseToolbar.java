/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gafactory.core.client.actions.Action;
import com.gafactory.core.client.events.PerformFilterEvent;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.base.AbstractListItem;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Iterator;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 28.03.13
 */
@SuppressWarnings({"PackageVisibleField", "UnusedParameters"})
public class BaseToolbar extends Composite implements HasWidgets, PerformFilterEvent.HasPerformFilterHandlers {

    /*===========================================[ INSTANCE VARIABLES ]===========*/
    @UiField
    TextBox search;
    @UiField
    Anchor remove;
    @UiField
    Anchor extendSearch;
    @UiField
    InputGroup searchAddOn;
    @UiField
    NavbarNav listItemContainer;
    @UiField
    DropDownMenu elseSubmenu;
    @UiField
    ListDropDown elseDropdown;
    @UiField
    NavbarBrand header;
    @UiField
    HTML rowCounter;
    @UiField
    NavbarText rowCounterNavbarText;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public BaseToolbar() {
        init();
    }


    protected void init() {
        initWidget(binder.createAndBindUi(this));

        remove.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                PerformFilterEvent.fire(BaseToolbar.this, new BaseFilter());
                search.setValue(null);
                search.setEnabled(true);
            }
        });

        search.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                PerformFilterEvent.fire(BaseToolbar.this, new BaseFilter(search.getText()));

            }
        });
    }

    /*===========================================[ CLASS METHODS ]================*/


    public void setSearchPanelVisible(Boolean visible) {
        if (visible != null && search != null) {
            search.setVisible(visible);
        }
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void add(Widget w) {
        listItemContainer.add(w);
    }

    @Override
    public void clear() {
        listItemContainer.clear();
    }

    @Override
    public boolean remove(Widget w) {
        return listItemContainer.remove(w);
    }

    @Override
    public Iterator<Widget> iterator() {
        return listItemContainer.iterator();
    }


    @Override
    public HandlerRegistration addPerformFilterHandler(PerformFilterEvent.PerformFilterHandler handler) {
        return addHandler(handler, PerformFilterEvent.getType());
    }


    public void setExtendSearch(String extend) {
        if (extend.equalsIgnoreCase("true")) {
            extendSearch.setVisible(true);
        } else {
            remove.setIcon(IconType.SEARCH);
        }
    }

    public void displayFilter(String filterDisplay) {

    }

    public void setSearchBoxPlaceholder(String searchBoxPlaceholder) {
        search.setPlaceholder(searchBoxPlaceholder);
    }

    /*===========================================[ INNER CLASSES ]================*/

    private static final Binder binder = GWT.create(Binder.class);

    public void hideSearch() {
        search.setVisible(false);
    }

    public void setExtendSearch(boolean extend) {
        if (extend) {
            extendSearch.setVisible(true);
        } else {
            remove.setIcon(IconType.SEARCH);
        }
    }

    public <T> void addAction(Action<T> action, AbstractListItem actionWidget) {
        addAction(action.isAdditional(), actionWidget);
    }

    public void addAction(boolean addition, AbstractListItem actionWidget) {
        addAction(addition, actionWidget, listItemContainer.getWidgetCount() - 1);
    }

    public void addAction(boolean addition, AbstractListItem actionWidget, int beforeIndex) {

        if (addition) {
            elseDropdown.setVisible(true);
            elseSubmenu.add(actionWidget);
        } else {
            listItemContainer.insert(actionWidget, beforeIndex);
        }
    }

    public void focusSearchBySymbol(Character symbol) {
        if (search.isVisible()) {
            search.setText("" + symbol);
            search.setFocus(true);

        }
    }

    public void setHeader(String displayHeader) {
        header.setText(displayHeader);
    }

    public void setSearchFieldText(String value) {
        search.setText(value);
    }

    public void setRowCount(int newRowCount) {
        rowCounterNavbarText.setVisible(true);
        rowCounter.setText("Элементов: " + NumberFormat.getDecimalFormat().format(newRowCount));
    }

    @SuppressWarnings("PackageVisibleInnerClass")
    interface Binder extends UiBinder<Widget, BaseToolbar> {

    }


}
