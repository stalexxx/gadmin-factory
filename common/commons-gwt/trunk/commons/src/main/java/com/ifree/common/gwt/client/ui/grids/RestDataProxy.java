package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.ifree.common.gwt.client.rest.CRUDRestService;
import com.ifree.common.gwt.client.rest.ListingRestService;
import com.ifree.common.gwt.client.ui.AbstractAsyncCallback;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResultBean;

import java.io.Serializable;

/**
 * Created by alex on 05.05.14.
 */
public class RestDataProxy<T,
        ID extends Serializable,
        REST extends CRUDRestService<T, ID>& ListingRestService<T>>
        extends BaseDataProxy<T> implements ItemLoader<T, ID> {

    protected final REST restSerivce;

    public RestDataProxy(REST restSerivce) {
        this.restSerivce = restSerivce;
    }

    @Override
    protected final RestAction<PagingLoadResultBean<T>> getAction(FilterPagingLoadConfigBean loadConfig) {
        return restSerivce.getItems(loadConfig);
    }

    @Override
    protected final RestAction<PagingLoadResultBean<T>> getAction() {
        return restSerivce.getItems();
    }


    @Override
    public void loadOne(ID id, AsyncCallback<T> callback) {
        restDispatch.execute(getFindOneAction(id), callback);
    }

    protected final RestAction<T> getFindOneAction(ID id) {
        return restSerivce.getItem(id);
    }

}
