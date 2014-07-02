package com.ifree.common.gwt.spring;

import com.ifree.common.gwt.client.rest.BaseResourcePaths;
import com.ifree.common.gwt.shared.SavingResult;
import com.ifree.common.gwt.shared.loader.FilterConfig;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResultBean;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.ValidationException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 01.07.14.
 */
public abstract class BaseJpaResource<ID extends Serializable, Entity_, EntityDto_, R extends JpaRepository<Entity_, ID> & JpaSpecificationExecutor<Entity_>>
        extends BaseResource<Entity_,EntityDto_> implements CRUDResource<EntityDto_, ID>,ListingResource {


    @Override
    protected Specification<Entity_> booleanSpecification(FilterConfig filter, String type) {
        return BaseSpecifications.booleanSpecification(getFilterHelper().<Boolean>getValue(type, filter.getValue()),
                filter.getField());
    }

    @Override
    protected Specification<Entity_> integerSpecification(FilterConfig filter, String type) {
        Integer value = getFilterHelper().<Integer>getValue(type, filter.getValue());

        return BaseSpecifications.integerSpecification(value, filter.getField());
    }

    @Override
    protected Specification<Entity_> stringSpecification(FilterConfig filter, String type) {
        return BaseSpecifications.baseStringIsLike(
                getFilterHelper().<String>getValue(type, filter.getValue()),
                filter.getField());
    }

    @Override
    public Response getItem(ID id) {
        Entity_ entity = getJpaRepository().findOne(id);
        EntityDto_ dto = getConversionService().convert(entity, getDtoClazz());
        return Response.ok(dto).build();
    }

    @Override
    @POST
    public Response save(EntityDto_ dto) {
        Entity_ entity = getConversionService().convert(dto, getEntityClazz());
        ID id = getId(entity);

        SavingResult<ID> savingResult;

        if (id != null && getJpaRepository().exists(id)) {
            savingResult = update(dto, entity, id);
        } else {
            savingResult = create(dto, entity);
        }

        return Response.ok(savingResult).build();
    }

    //  @Override
    public SavingResult<ID> create(EntityDto_ dto, Entity_ entity) {

        Entity_ result = null;

        SavingResult<ID> savingResult;

        try {
            validateOnCreation(dto);

            doPreSave(dto, entity, null);

            result = getJpaRepository().save(entity);

            ID id = getId(result);

            doAdditionalSave(dto, result, id);

            return new SavingResult<ID>(id);
        } catch (ValidationException e) {
            return new SavingResult<ID>(e.getMessage());
        }

    }

    protected void doAdditionalSave(EntityDto_ dto, Entity_ result, ID id) {}

    protected void validateOnCreation(EntityDto_ dto) throws ValidationException {}

    protected void doPreSave(EntityDto_ dto, Entity_ entity, ID id) {}

    // @Override
     public SavingResult<ID> update(EntityDto_ dto, Entity_ entity, ID id) {

         Entity_ toUpdate = getJpaRepository().findOne(id);
         copyFields(entity, toUpdate);

         doPreSave(dto, toUpdate, id);


         Entity_ saved = getJpaRepository().save(toUpdate);

         doAdditionalSave(dto, saved, id);

         return new SavingResult<ID>(getId(saved));

     }

    protected abstract void copyFields(Entity_ from, Entity_ to);

    protected abstract ID getId(Entity_ entity);

    @Override
     public Response delete(ID id) {
         try {
             getJpaRepository().delete(id);
             return Response.ok(true).build();
         } catch (Exception e) {
             getLogger().error(String.format("cant remove %d", id), e);

             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
         }
     }

    protected abstract Logger getLogger();


    @Override
     protected Page<Entity_> findAll(FilterPagingLoadConfigBean config) {
         return getJpaRepository().findAll(pageable(config));
     }

    @Override
     protected Page<Entity_> findAll(Specifications<Entity_> spec, FilterPagingLoadConfigBean config) {
         return getJpaRepository().findAll(spec, pageable(config));
     }

    @Override
     @GET
     public Response getItems() {
         List<EntityDto_> dtos = getConversionService().transformToList(getJpaRepository().findAll(), getDtoClazz());
         return Response.ok(new PagingLoadResultBean<EntityDto_>(dtos, dtos.size(), 0)).build();
     }

    @Override
     @POST
     @Path(BaseResourcePaths.FILTER)
     public Response getItems(FilterPagingLoadConfigBean config) {
         PagingLoadResultBean<EntityDto_> resultBean = getPagingLoadResultBean(config);
         return Response.ok(resultBean).build();
     }

    protected abstract R getJpaRepository();
}
