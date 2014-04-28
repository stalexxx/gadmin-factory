package com.ifree.common.gwt.spring;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ifree.common.gwt.client.ui.grids.FilterHelper;
import com.ifree.common.gwt.shared.SortDir;
import com.ifree.common.gwt.shared.SortInfoBean;
import com.ifree.common.gwt.shared.loader.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.Nullable;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by alex on 25.04.14.
 */
@Produces(MediaType.APPLICATION_JSON)
public abstract class BaseResource<Entity_, EntityDto_> {
    public final Specification<Entity_> NONE_SPECIFICATION = null;

    protected PagingLoadResultBean<EntityDto_> getPagingLoadResultBean(FilterPagingLoadConfigBean config) {
        Specifications<Entity_> spec = null;

        List<FilterConfigBean> filters = config.getFilters();
        for (FilterConfigBean filter : filters) {
            Specification<Entity_> specification = createSpecification(filter);
            spec = andOrWhere(spec, specification);
        }

        Page<Entity_> page;
        if (spec != null) {
            page = findAll(spec, config);
        } else {
            page = findAll(config);
        }

        List<EntityDto_> dtos = getConversionService().transformToList(page.getContent(), getClazz());

        return new PagingLoadResultBean<EntityDto_>(dtos, (int) page.getTotalElements(), config.getOffset());
    }

    protected abstract BaseConversionService getConversionService();

    protected abstract Class<EntityDto_> getClazz();

    protected abstract Page<Entity_> findAll(FilterPagingLoadConfigBean config);

    protected abstract Page<Entity_> findAll(Specifications<Entity_> spec, FilterPagingLoadConfigBean config);

    protected Pageable pageable(FilterPagingLoadConfigBean config) {
        int page = config.getOffset() / config.getLimit();

        if (hasSortInfo(config)) {
            return new PageRequest(page, config.getLimit(), sort(config));
        } else {
            return new PageRequest(page, config.getLimit());
        }
    }

    private boolean hasSortInfo(FilterPagingLoadConfigBean config) {
        return config.getSortInfo() != null && !config.getSortInfo().isEmpty();
    }

    private Sort sort(FilterPagingLoadConfigBean config) {
        return new Sort(Lists.transform(config.getSortInfo(), new Function<SortInfoBean, Sort.Order>() {
            @Nullable
            @Override
            public Sort.Order apply(@Nullable SortInfoBean input) {
                Sort.Direction direction = SortDir.ASC.equals(input.getSortDir()) ? Sort.Direction.ASC : Sort.Direction.DESC;
                return input != null ? new Sort.Order(
                        direction,
                        input.getSortField())
                        : null;
            }
        }));
    }

    private Specification<Entity_> createSpecification(FilterConfig filter) {
        if (filter.getValue() != null) {

            String type = filter.getType();

            if (type.equals(FilterHelper.STRING_TYPE)) {

                if (filter.getValue().isEmpty()) {
                    return NONE_SPECIFICATION;

                }
                return stringLike(filter, type);


            } else if (type.equals(FilterHelper.BOOLEAN_TYPE)) {

                return booleanSpecification(filter, type);

            } else if (type.equals(FilterHelper.INTEGER_TYPE)) {

                return integerSpecification(filter, type);

            } else if (type.equals(FilterHelper.FLOAT_TYPE)) {

                throw new UnsupportedOperationException();
            }
        }
        return NONE_SPECIFICATION;
    }

    protected abstract Specification<Entity_> booleanSpecification(FilterConfig filter, String type);

    protected abstract Specification<Entity_> integerSpecification(FilterConfig filter, String type);

    protected abstract FilterHelper getFilterHelper();

    protected abstract Specification<Entity_> stringLike(FilterConfig filter, String type);

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