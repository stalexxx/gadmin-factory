package com.ifree.common.gwt.client.rest;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.ifree.common.gwt.shared.SavingResult;

import javax.ws.rs.*;
import java.io.Serializable;

/**
 * Created by alex on 28.04.14.
 */
public interface CRUDRestService<T, ID extends Serializable> {

    @GET @Path("/{id}")
    RestAction<T> getItem(@PathParam("id") ID id);

    @POST
    RestAction<SavingResult<ID>> save(T dto);

    @DELETE @Path("/{id}")
    RestAction<Boolean> delete(@PathParam("id") ID id);
}
