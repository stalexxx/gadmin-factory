package com.gafactory.core.spring;

import com.gafactory.core.client.rest.BaseResourcePaths;
import com.gafactory.core.shared.BaseFilterFields;
import com.gafactory.core.shared.SavingResult;
import com.gafactory.core.shared.loader.FilterConfig;
import com.gafactory.core.shared.loader.FilterPagingLoadConfigBean;
import com.gafactory.core.shared.loader.PagingLoadResultBean;
import com.gafactory.core.shared.types.DateInterval;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    protected Specification<Entity_> dateIntervalSecification(FilterConfig filter, String field) {
        DateInterval value = getFilterHelper().getValue(filter.getType(), filter.getValue());

        if (value != null ) {
            return BaseSpecifications.dateIntervalSpecification(value, field);
        }
        return null;
    }

    @Override
    protected Specification<Entity_> integerSpecification(FilterConfig filter, String type) {
        Integer value = getFilterHelper().<Integer>getValue(type, filter.getValue());
        return BaseSpecifications.numberSpecification(value, filter.getField());
    }

    @Override
    protected Specification<Entity_> longSpecification(FilterConfig filter, String type) {

        Long value = getFilterHelper().<Long>getValue(type, filter.getValue());

        return BaseSpecifications.numberSpecification(value, filter.getField());
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
        boolean exist = exist(dto, entity);

        SavingResult<ID> savingResult;

        if (exist &&  id != null && getJpaRepository().exists(id)) {
            savingResult = update(dto, entity, id);
        } else {
            savingResult = create(dto, entity);
        }

        return Response.ok(savingResult).build();
    }

    protected boolean exist(EntityDto_ dto, Entity_ entity) {
        ID id = getId(entity);
        return id != null;
    }

    //  @Override
    public SavingResult<ID> create(EntityDto_ dto, Entity_ entity) {

        Entity_ result;

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

    // @Override
    public SavingResult<ID> update(EntityDto_ dto, Entity_ entity, ID id) {

        Entity_ toUpdate = getJpaRepository().findOne(id);
        merge(entity, toUpdate);

        try {
            validateOnUpdate(dto);

            doPreSave(dto, toUpdate, id);

            Entity_ saved = getJpaRepository().save(toUpdate);

            doAdditionalSave(dto, saved, id);

            return new SavingResult<ID>(getId(saved));
        } catch (ValidationException e) {
            return new SavingResult<ID>(e.getMessage());
        }
    }

    protected void doAdditionalSave(EntityDto_ dto, Entity_ result, ID id) {}

    protected void validateOnCreation(EntityDto_ dto) throws ValidationException {}
    protected void validateOnUpdate(EntityDto_ dto) throws ValidationException {}

    protected void doPreSave(EntityDto_ dto, Entity_ entity, ID id) {}

    protected abstract void merge(Entity_ from, Entity_ to);

    protected abstract ID getId(Entity_ entity);

    @Override
     public Response delete(ID id) {
         try {
             getJpaRepository().delete(id);
             return Response.ok(new SavingResult<>()).build();
         } catch (Exception e) {
             getLogger().error(String.format("cant remove %s", String.valueOf(id)), e);

             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
         }
     }


    protected abstract Logger getLogger();

    @Override
    protected Page<Entity_> findAll(FilterPagingLoadConfigBean config) {
        if (config.getLimit() > 0) {
            return getJpaRepository().findAll(pageable(config));
        } else {
            List<Entity_> all = hasSortInfo(config) ?
                    getJpaRepository().findAll( sort(config)) :
                    getJpaRepository().findAll();

            return new PageImpl<Entity_>(all);
        }
    }

    @Override
    protected Page<Entity_> findAll(Specifications<Entity_> spec, FilterPagingLoadConfigBean config) {

        if (config.getLimit() > 0) {
            return getJpaRepository().findAll(spec, pageable(config));
        } else {

            List<Entity_> all = hasSortInfo(config) ?
                getJpaRepository().findAll(spec, sort(config)) :
                getJpaRepository().findAll(spec);

            return new PageImpl<Entity_>(all);
        }

    }

    @Override
     @GET
     public Response getItems() {
         List<EntityDto_> dtos = getConversionService().transformToListNotNull(getJpaRepository().findAll(), getDtoClazz());
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

    @Override
    protected Specification<Entity_> createExactSpecification(String field, FilterConfig filter) {
        if (filter.getValue() != null) {
            if (BaseFilterFields.EXCLUDE_ID_LIST.equals(field)) {
                List<String> value = getFilterHelper().getValue(filter.getType(), filter.getValue());
                return BaseSpecifications.excludeIdListSpecification(value, "id"); //todo
            }
        }

        return super.createExactSpecification(field, filter);
    }
}
