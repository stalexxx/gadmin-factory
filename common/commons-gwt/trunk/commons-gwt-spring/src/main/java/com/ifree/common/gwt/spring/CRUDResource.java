package com.ifree.common.gwt.spring;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;

/**
 * Created by alex on 28.04.14.
 */
public interface CRUDResource<T, ID extends Serializable> {

    @GET
    @Path("/{id}")
    Response getItem(@PathParam("id") ID id);

    @POST
    //RestAction<ID> create(T dto);
    Response save(T dto);

    /*@PUT
//    RestAction<Boolean> update(T dto);
    Response update(T dto);*/

    @DELETE @Path("/{id}")
//    RestAction<Boolean> delete(@PathParam("id") ID id);
    Response delete(@PathParam("id") ID id);
}

