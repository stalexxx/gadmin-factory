/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 07.06.13
 */
@SuppressWarnings("PackageVisibleField")
public class AlertsPanel extends Composite {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final int DELAY_MS = 10000;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @UiField
    FlowPanel container;

    /*===========================================[ CONSTRUCTORS ]=================*/
    Binder binder = GWT.create(Binder.class);

    public AlertsPanel() {
        initWidget(binder.createAndBindUi(this));

    }

    /*===========================================[ CLASS METHODS ]================*/

    public void addAlert(String html, AlertType type) {
        addAlert(html, type, DELAY_MS);
    }
    public void addAlert(String html, AlertType type, int timeToClose) {
        final Alert w = new Alert(html, type);
        //w.setAnimation(true);
        w.setDismissable(true);
        container.add(w);

        //w.clear();

        if (timeToClose != 0) {
            Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    w.close();
                    return false;
                }
            }, timeToClose);

        }
    }

    /*===========================================[ INNER CLASSES ]================*/

    public interface Binder extends UiBinder<Widget, AlertsPanel> {

    }

}
