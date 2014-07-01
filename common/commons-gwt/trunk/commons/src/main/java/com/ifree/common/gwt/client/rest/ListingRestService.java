package com.ifree.common.gwt.client.rest;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResultBean;

import javax.ws.rs.*;

/**
 * Created by alex on 28.04.14.
 */
public interface ListingRestService<T> {

    @GET
    RestAction<PagingLoadResultBean<T>> getItems();

    @POST
    @Path(BaseResourcePaths.FILTER)
    RestAction<PagingLoadResultBean<T>> getItems(FilterPagingLoadConfigBean loadConfig);
}
