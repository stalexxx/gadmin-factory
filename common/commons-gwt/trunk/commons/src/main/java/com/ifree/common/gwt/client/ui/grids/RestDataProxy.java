package com.ifree.common.gwt.client.ui.grids;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.ifree.common.gwt.client.rest.CRUDRestService;
import com.ifree.common.gwt.client.rest.ListingRestService;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResultBean;

import java.io.Serializable;

/**
 * Created by alex on 05.05.14.
 */
public class RestDataProxy<T,
        ID extends Serializable,
        REST extends CRUDRestService<T, ID>& ListingRestService<T>> extends BaseDataProxy<T> {

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

}
