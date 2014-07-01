package com.ifree.common.gwt.spring;

import com.ifree.common.gwt.client.rest.BaseResourcePaths;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;

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

