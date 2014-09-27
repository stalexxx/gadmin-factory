/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gafactory.core.client.events.ShowAlertEvent;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 21.03.14
 */
public abstract class AlertingAsyncCallback<T> implements AsyncCallback<T> {

    private EventBus eventBus;

    protected AlertingAsyncCallback(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onFailure(Throwable caught) {
        eventBus.fireEvent(new ShowAlertEvent(caught.getMessage(), AlertType.DANGER));
    }
}
