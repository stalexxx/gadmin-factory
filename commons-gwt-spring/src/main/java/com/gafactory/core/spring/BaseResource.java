package com.gafactory.core.spring;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.gafactory.core.client.ui.grids.BaseFilterHelper;
import com.gafactory.core.shared.SortDir;
import com.gafactory.core.shared.SortInfoBean;
import com.gafactory.core.shared.loader.FilterConfig;
import com.gafactory.core.shared.loader.FilterConfigBean;
import com.gafactory.core.shared.loader.FilterPagingLoadConfigBean;
import com.gafactory.core.shared.loader.PagingLoadResultBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.Nullable;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 25.04.14.
 */
@Produces(MediaType.APPLICATION_JSON)
public abstract class BaseResource<Entity_, EntityDto_> {
    public static final Function<SortInfoBean, Sort.Order> SORT_INFO_BEAN_ORDER_FUNCTION = new Function<SortInfoBean, Sort.Order>() {
        @Nullable
        @Override
        public Sort.Order apply(SortInfoBean input) {
            Sort.Direction direction = SortDir.ASC.equals(input.getSortDir()) ? Sort.Direction.ASC : Sort.Direction.DESC;
            return new Sort.Order(
                    direction,
                    input.getSortField());
        }
    };
    public final Specification<Entity_> NONE_SPECIFICATION = null;

    protected PagingLoadResultBean<EntityDto_> getPagingLoadResultBean(FilterPagingLoadConfigBean config) {
        Specifications<Entity_> spec = createInitSpecification();

        List<FilterConfigBean> filters = config.getFilters();
        for (FilterConfigBean filter : filters) {
            Specification<Entity_> specification = createSpecification(filter);
            spec = andOrWhere(spec, specification);
        }

        spec = andOrWhere(spec, postProcessSpecifications());

        Page<Entity_> page;
        if (spec != null) {
            page = findAll(spec, config);
        } else {
            page = findAll(config);
        }

        List<Entity_> entities = page.getContent();
        List<EntityDto_> dtos = getConversionService().transformToList(entities, getDtoClazz());

        postProcessPagingLoadDtos(dtos, entities);

        return new PagingLoadResultBean<EntityDto_>(dtos, (int) page.getTotalElements(), config.getOffset());
    }

    /**
     * @return
     */
    protected Specification<Entity_> postProcessSpecifications() {
        return null;
    }

    protected Specifications<Entity_> createInitSpecification() {
        return null;
    }

    protected void postProcessPagingLoadDtos(List<EntityDto_> dtos, List<Entity_> entities) {

        Iterator<EntityDto_> iteratorDto = dtos.iterator();

        for (Entity_ next : entities) {
            postProcessPagingLoadDto(iteratorDto.next(), next);
        }
    }

    protected void postProcessPagingLoadDto(EntityDto_ entityDto, Entity_ entity) {

    }

    protected abstract BaseConversionService getConversionService();

    protected abstract Class<EntityDto_> getDtoClazz();

    protected abstract Class<Entity_> getEntityClazz();

    protected abstract Page<Entity_> findAll(FilterPagingLoadConfigBean config);

    protected abstract Page<Entity_> findAll(Specifications<Entity_> spec, FilterPagingLoadConfigBean config);

    protected Pageable pageable(FilterPagingLoadConfigBean config) {
        Preconditions.checkArgument(config.getLimit() > 0);

        int page = config.getOffset() / config.getLimit();
        int additional = config.getOffset() % config.getLimit();

        if (hasSortInfo(config)) {
            return new PageRequest(page, config.getLimit() + additional, sort(config));
        } else {
            return new PageRequest(page, config.getLimit() + additional);
        }
    }

    protected boolean hasSortInfo(FilterPagingLoadConfigBean config) {
        return config.getSortInfo() != null && !config.getSortInfo().isEmpty();
    }

    protected Sort sort(FilterPagingLoadConfigBean config) {
        return new Sort(Lists.transform(config.getSortInfo(), SORT_INFO_BEAN_ORDER_FUNCTION));
    }


    protected Specification<Entity_> createSpecification(FilterConfig filter) {

        Specification<Entity_> specification = createExactSpecification(filter.getField(), filter);
        if (specification != null) {
            return specification;
        }

        if (filter.getValue() != null) {

            String type = filter.getType();

            if (type.equals(BaseFilterHelper.STRING_TYPE)) {

                if (filter.getValue().isEmpty()) {
                    return NONE_SPECIFICATION;

                }
                return stringSpecification(filter, type);

            } else if (type.equals(BaseFilterHelper.BOOLEAN_TYPE)) {

                return booleanSpecification(filter, type);

            } else if (type.equals(BaseFilterHelper.LONG_TYPE)) {

                return longSpecification(filter, type);

            } else if (type.equals(BaseFilterHelper.INTEGER_TYPE)) {

                return integerSpecification(filter, type);

            } else if (type.equals(BaseFilterHelper.FLOAT_TYPE)) {

                throw new UnsupportedOperationException();
            } else if (type.equals(BaseFilterHelper.DATE_INTERVAL_TYPE)) {

                return dateIntervalSecification(filter, filter.getField());
            } else {
                return createSpecificationByUnfound(filter);
            }
        }
        return NONE_SPECIFICATION;
    }

    /**
     * This method intended to be overriden when exact type of specification should be provided;
     * Использовать этот метод, когда надо переопределить спецификацию для определенного поля фильтрации
     *
     * @param field
     * @param filter
     * @return
     */
    protected Specification<Entity_> createExactSpecification(String field, FilterConfig filter) {
        return NONE_SPECIFICATION;
    }

    protected abstract Specification<Entity_> createSpecificationByUnfound(FilterConfig filter);

    protected abstract Specification<Entity_> dateIntervalSecification(FilterConfig interval, String field);

    protected abstract Specification<Entity_> booleanSpecification(FilterConfig filter, String type);

    protected abstract Specification<Entity_> integerSpecification(FilterConfig filter, String type);

    protected abstract Specification<Entity_> longSpecification(FilterConfig filter, String type);

    protected abstract BaseFilterHelper getFilterHelper();

    protected abstract Specification<Entity_> stringSpecification(FilterConfig filter, String type);

    protected Specifications<Entity_> andOrWhere(Specifications<Entity_> specs, Specification<Entity_> specification) {
        if (specification != NONE_SPECIFICATION) {
            if (specs != null) {
                return specs.and(specification);
            } else {

                return Specifications.where(specification);
            }
        }
        return specs;
    }
}
