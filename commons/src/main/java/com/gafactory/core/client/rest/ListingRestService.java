package com.gafactory.core.client.rest;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gafactory.core.shared.loader.FilterPagingLoadConfigBean;
import com.gafactory.core.shared.loader.PagingLoadResultBean;

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
