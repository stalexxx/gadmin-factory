/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.rpc.shared.NoResult;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.ifree.common.gwt.client.ui.AbstractAsyncCallback;

/**
* @author Alexander Ostrovskiy (a.ostrovskiy)
* @since 18.03.14
*/
public class CountBackAsyncCallback implements AsyncCallback<NoResult> {
    private ProxyPlace<?> proxy;
    private Presenter<?, ?> presenter;
    private int counter;

    public CountBackAsyncCallback(ProxyPlace<?> proxy, Presenter<?, ?> presenter) {
        this.proxy = proxy;
        this.presenter = presenter;
        this.counter = counter;
    }

    @Override
    public void onFailure(Throwable caught) {
        proxy.manualRevealFailed();
    }

    @Override
    public void onSuccess(NoResult result) {
        counter--;
        if (counter == 0) {
            proxy.manualReveal(presenter);
        }
    }

    public <T> AsyncCallback<T> createCountbackWrapper(final AbstractAsyncCallback<T> callback) {
        counter += 1;
        return new AsyncCallback<T>() {
            @Override
            public void onFailure(Throwable caught) {
                CountBackAsyncCallback.this.onFailure(caught);
            }

            @Override
            public void onSuccess(T result) {
                callback.onSuccess(result);
                CountBackAsyncCallback.this.onSuccess(null);

            }
        };
    }


}
