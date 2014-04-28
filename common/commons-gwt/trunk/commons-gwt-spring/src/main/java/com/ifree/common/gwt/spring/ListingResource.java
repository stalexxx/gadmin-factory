package com.ifree.common.gwt.spring;

import com.ifree.common.gwt.client.rest.BasePaths;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResultBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 28.04.14.
 */
public interface ListingResource {

    @GET
    public Response getItems();

    @POST
    @Path(BasePaths.FILTER)
    public Response getItems(FilterPagingLoadConfigBean config);
}

