package com.gafactory.core.client.rest;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gafactory.core.shared.RemovingResult;
import com.gafactory.core.shared.SavingResult;

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
    RestAction<RemovingResult> delete(@PathParam("id") ID id);
}
