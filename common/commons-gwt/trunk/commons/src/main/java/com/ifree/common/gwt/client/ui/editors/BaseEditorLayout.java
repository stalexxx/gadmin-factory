/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FormGroup;

import java.util.Iterator;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 16.05.13
 */
@SuppressWarnings({"PackageVisibleField", "UnusedParameters"})
public class BaseEditorLayout extends Composite implements HasWidgets {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Binder binder = GWT.create(Binder.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @UiField
    SimplePanel toolbar;
    @UiField
    SimplePanel controlGroup;
    @UiField
    SimplePanel buttonGroupContainer;
    @UiField
    Anchor back;
    @UiField
    Column left;
    @UiField
    Column right;
    @UiField
    LayoutPanel layoutPanel;


    private BaseEditorUiHandlers uiHandlers;

    private ButtonGroup buttonGroup;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @UiConstructor
    public BaseEditorLayout(String leftSize, String rightSize) {
        initWidget(binder.createAndBindUi(this));

        setLeftSize(leftSize);
        setRightSize(rightSize);
    }

    public void setLeftSize(String size) {
        left.setSize(size);
    }

    public void setRightSize(String size) {
        right.setSize(size);
    }

    /*===========================================[ CLASS METHODS ]================*/

    @UiHandler("back")
    public void onBackClick(ClickEvent clickEvent) {
        uiHandlers.onBack();
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void add(final Widget w) {

        if (w instanceof Left) {
            left.add(w);
        } else if (w instanceof Right) {
            right.add(w);
        } else if (w instanceof ButtonGroup) {
            buttonGroupContainer.setWidget(w);
            buttonGroup = ((ButtonGroup) w);
            w.addAttachHandler(new AttachEvent.Handler() {
                @Override
                public void onAttachOrDetach(AttachEvent event) {
                    scheduleUpdateToolbarSize(w);
                }
            });
        } else if (w instanceof FormGroup) {
            controlGroup.setWidget(w);
        } else {
            throw new UnsupportedOperationException("unsupprted widget");
        }
    }


    public void scheduleUpdateToolbarSize() {
        scheduleUpdateToolbarSize(buttonGroupContainer);

    }

    private void scheduleUpdateToolbarSize(final Widget w) {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                int offsetWidth = getBtnGroupWidth();

                layoutPanel.setWidgetLeftRight(controlGroup, 30, Style.Unit.PX, offsetWidth, Style.Unit.PX);
                layoutPanel.setWidgetRightWidth(buttonGroupContainer, 0, Style.Unit.PX, offsetWidth, Style.Unit.PX);

            }


        });


    }

    private int getBtnGroupWidth() {
        if (buttonGroup != null) {
            int count = buttonGroup.getWidgetCount();

            int size = 0;

            for (int i = 0; i < count; i++) {
                Widget btn = buttonGroup.getWidget(i);
                if (btn.isVisible()) {
                    size += btn.getOffsetWidth();
                }
            }
            return size;
        }
        return 0;
    }

    @Override
    public void clear() {
        left.clear();
        right.clear();
    }

    @Override
    public boolean remove(Widget w) {
        return true;
    }

    @Override
    public Iterator<Widget> iterator() {
        return left.iterator();
    }

    /*===========================================[ GETTER/SETTER ]================*/

    public BaseEditorUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(BaseEditorUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void hideBackButton() {
        back.setVisible(false);
    }

    /*===========================================[ INNER CLASSES ]================*/

    public interface Binder extends UiBinder<Widget, BaseEditorLayout> {
    }


}
