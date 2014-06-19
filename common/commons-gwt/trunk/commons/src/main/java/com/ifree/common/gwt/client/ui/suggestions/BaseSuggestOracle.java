package com.ifree.common.gwt.client.ui.suggestions;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.client.ui.grids.BaseFilterHelper;
import com.ifree.common.gwt.client.utils.StringUtils;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfig;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfigBean;
import com.ifree.common.gwt.shared.loader.PagingLoadResult;

import javax.annotation.Nullable;

/**
* Created by alex on 13.05.14.
*/
@SuppressWarnings("Convert2Diamond")
class BaseSuggestOracle<T> extends SuggestOracle {

    private final BaseDataProxy<T> dataProxy;
    private Renderer<T> renderer;
    private ValueProvider<T, String> searchField;


    public BaseSuggestOracle(BaseDataProxy<T> dataProxy, Renderer<T> renderer, ValueProvider<T, String> searchField) {
        this.dataProxy = dataProxy;
        this.renderer = renderer;
        this.searchField = searchField;
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        dataProxy.load(createFilterBean(request), new PagingLoadResultCallback<T>(callback, request, renderer));
    }

    @Override
    public void requestDefaultSuggestions(Request request, Callback callback) {
        dataProxy.load(createFilterBean(request), new PagingLoadResultCallback<T>(callback, request, renderer));
    }

    private FilterPagingLoadConfig createFilterBean(Request request) {

        FilterPagingLoadConfigBean bean = new FilterPagingLoadConfigBean();
        bean.setLimit(request.getLimit());
        bean.setOffset(0);
        FilterConfigBean configBean = new FilterConfigBean();

        configBean.setValue(request.getQuery());
        configBean.setField(searchField.getPath());
        configBean.setType(BaseFilterHelper.STRING_TYPE);

        bean.setFilters(Lists.newArrayList(configBean));
        return bean;
    }

    @SuppressWarnings("Convert2Lambda")
    private static class PagingLoadResultCallback<T> implements com.google.gwt.core.client.Callback<PagingLoadResult<T>, Throwable> {
        private final Callback callback;
        private final Request request;
        private Renderer<T> renderer;

        public PagingLoadResultCallback(Callback callback, Request request, Renderer<T> renderer) {
            this.callback = callback;
            this.request = request;
            this.renderer = renderer;
        }

        @Override
        public void onFailure(Throwable throwable) {
            //todo
        }

        @Override
        public void onSuccess(PagingLoadResult<T> tPagingLoadResult) {
            final RegExp regExp = StringUtils.prepareSimplePattern(request.getQuery());

            callback.onSuggestionsReady(request, new Response(Collections2.transform(tPagingLoadResult.getData(), new Function<T, Suggestion>() {
                @Nullable
                @Override
                public Suggestion apply(@Nullable T input) {
                    if (input != null) {

                        return new EmphasizedSuggestion<T>(input, renderer, regExp);
                    }
                    return null;

                }
            })));
        }
    }

}
