package com.gafactory.core.spring;

import com.gafactory.core.client.rest.BaseResourcePaths;
import com.gafactory.core.shared.loader.FilterPagingLoadConfigBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by alex on 28.04.14.
 */
public interface ListingResource {

    @GET
    public Response getItems();

    @POST
    @Path(BaseResourcePaths.FILTER)
    public Response getItems(FilterPagingLoadConfigBean config);
}

