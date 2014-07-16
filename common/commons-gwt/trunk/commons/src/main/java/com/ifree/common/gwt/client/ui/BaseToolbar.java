/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.ui.lists.BaseFilterPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.base.AbstractListItem;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.NavbarType;

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
    Column filterPanel;
    @UiField
    DropDownMenu elseSubmenu;
    @UiField
    ListDropDown elseDropdown;
    @UiField
    NavbarBrand header;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public BaseToolbar() {
        init();
    }


    protected void init() {
        initWidget(binder.createAndBindUi(this));
        filterPanel.addStyleName(NavbarType.DEFAULT.getCssName());


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
        if (w instanceof AbstractPager) {
            //pagerContainer.add(w);
        } else if (w instanceof BaseFilterPanel) {
            setFilterPanel(w);

        } else {
            if (w instanceof Action) {
                Action action = (Action) w;
                
            }
        }
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

    public void setFilterPanel(IsWidget panel) {
        filterPanel.setVisible(true);
        filterPanel.add(panel);
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

        if (action.isAdditional()) {
            elseDropdown.setVisible(true);
            elseSubmenu.add(actionWidget);
        } else {
            listItemContainer.insert(actionWidget, listItemContainer.getWidgetCount() - 1);
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

    @SuppressWarnings("PackageVisibleInnerClass")
    interface Binder extends UiBinder<Widget, BaseToolbar> {

    }


}
