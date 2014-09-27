package com.gafactory.core.client.ui.grids;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gafactory.core.client.rest.CRUDRestService;
import com.gafactory.core.client.rest.ListingRestService;
import com.gafactory.core.shared.BaseFilterFields;
import com.gafactory.core.shared.SortInfoBean;
import com.gafactory.core.shared.loader.FilterConfigBean;
import com.gafactory.core.shared.loader.FilterPagingLoadConfig;
import com.gafactory.core.shared.loader.FilterPagingLoadConfigBean;
import com.gafactory.core.shared.loader.PagingLoadResultBean;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05.05.14.
 */
public class RestDataProxy<T,
        ID extends Serializable,
        REST extends CRUDRestService<T, ID>& ListingRestService<T>>
        extends BaseDataProxy<T> implements ItemLoader<T, ID> {

    protected final REST restSerivce;

    public RestDataProxy(REST restSerivce) {
        this.restSerivce = restSerivce;
    }

    private List<ID> excludeSet;

    @Override
    protected final RestAction<PagingLoadResultBean<T>> getAction(FilterPagingLoadConfigBean loadConfig) {
        applyConstraints(loadConfig);
        return restSerivce.getItems(loadConfig);
    }

    @Override
    protected final RestAction<PagingLoadResultBean<T>> getAction() {

        FilterPagingLoadConfigBean loadConfig = new FilterPagingLoadConfigBean();
        loadConfig.setLimit(0);

        applyConstraints(loadConfig);

        return restSerivce.getItems(loadConfig);
    }


    @Override
    public void loadOne(ID id, AsyncCallback<T> callback) {
        restDispatch.execute(getFindOneAction(id), callback);
    }

    protected final RestAction<T> getFindOneAction(ID id) {
        return restSerivce.getItem(id);
    }

    protected void applyConstraints(FilterPagingLoadConfigBean loadConfig) {
        List<FilterConfigBean> constraints = createConstraints();

        for (FilterConfigBean constraint : constraints) {
            loadConfig.addFilter(constraint);
        }

    }

    @NotNull
    protected List<FilterConfigBean> createConstraints() {
        ArrayList<FilterConfigBean> filterConfigBeans = Lists.newArrayList();

        if (excludeSet != null && !excludeSet.isEmpty()) {
            FilterConfigBean filterConfigBean = new FilterConfigBean();
            filterConfigBean.setType(BaseFilterHelper.STRING_LIST_TYPE);
            filterConfigBean.setValue(Joiner.on(",").join(excludeSet));
            filterConfigBean.setField(BaseFilterFields.EXCLUDE_ID_LIST);
            filterConfigBeans.add(filterConfigBean);
        }

        return filterConfigBeans;
    }


    protected FilterPagingLoadConfig prepareLoadConfig(FilterPagingLoadConfig loadConfig) {
        FilterPagingLoadConfig config = super.prepareLoadConfig(loadConfig);

        SortInfoBean sortColumn = getSecondSortColumn();

        if (sortColumn != null) {

            List<SortInfoBean> sortInfo = config != null ? config.getSortInfo() : null;
            if (sortInfo == null) {
                sortInfo = Lists.newArrayList();

            }

            if (!contains(sortInfo, sortColumn.getSortField())) {
                sortInfo.add(sortColumn);
            }
        }

        return config;
    }

    protected SortInfoBean getSecondSortColumn() {
        return null;
    }

    private boolean contains(List<SortInfoBean> sortInfo, String field) {
        for (SortInfoBean sortInfoBean : sortInfo) {
            if (field.equals(sortInfoBean.getSortField())) {
                return true;
            }
        }
        return false;
    }

    public void setExcluded(List<ID> list) {
        excludeSet = list;

    }
}
