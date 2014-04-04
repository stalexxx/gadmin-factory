/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
*
* @author Alexander Ostrovskiy (a.ostrovskiy)
* @since 18.03.14
*/
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {

    }
}
