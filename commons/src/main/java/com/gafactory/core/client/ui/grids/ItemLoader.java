package com.gafactory.core.client.ui.grids;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.io.Serializable;

/**
 * Created by alex on 22.05.14.
 */
public interface ItemLoader <T,
        ID extends Serializable> {

    void loadOne(ID id, AsyncCallback<T> callback);
}
