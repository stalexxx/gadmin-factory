/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.application;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.NotifyingAsyncCallback;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 21.03.14
 */
public abstract class AlertingNotifingAsyncCallback<T> extends NotifyingAsyncCallback<T>  {

   // private EventBus eventBus;

    public AlertingNotifingAsyncCallback(EventBus eventBus) {
        super(eventBus);
        prepare();
    }

}
