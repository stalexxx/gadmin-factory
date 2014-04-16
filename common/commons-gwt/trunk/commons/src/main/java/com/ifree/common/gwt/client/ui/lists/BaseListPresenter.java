/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.client.ui.grids.BaseFilterConfigBuilder;
import com.ifree.common.gwt.client.ui.grids.PagingSortingFilteringDataProvider;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.application.security.CurrentUser;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfig;
import com.ifree.common.gwt.shared.loader.FilterPagingLoader;
import com.ifree.common.gwt.shared.loader.PagingLoadResult;
import com.ifree.common.gwt.shared.loader.PagingLoader;

import javax.annotation.Nullable;


/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.07.13
 */
@SuppressWarnings("TypeParameterNamingConvention")
public abstract class BaseListPresenter<T,
                                        Filter_ extends BaseFilter,
                                        View_ extends ListView<T, Filter_>,
                                        Proxy_ extends Proxy<?>
                                        >
        extends Presenter<View_, Proxy_>
        implements ColumnSortEvent.Handler, SelectionChangeEvent.Handler, ListUiHandler<T, Filter_ > {

    @Inject
    protected PlaceManager placeManager;

    @Inject
    private CurrentUser currentUser;

    @Inject
    protected RestDispatch restDispatch;

    protected final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader;
    protected final PagingSortingFilteringDataProvider<T, Filter_> provider;

    protected BaseListPresenter(EventBus eventBus, View_ view, Proxy_ proxy,
                                GwtEvent.Type<RevealContentHandler<?>> slot,
                                BaseDataProxy<T> dataProxy) {
        super(eventBus, view, proxy, slot);
        loader = new FilterPagingLoader(dataProxy);
        provider = createProvider(view);

    }

    private PagingSortingFilteringDataProvider<T, Filter_> createProvider(View_ view) {
        return new PagingSortingFilteringDataProvider<T, Filter_>(loader, view, createFilterConfigBuilder());
    }

    protected BaseFilterConfigBuilder<Filter_> createFilterConfigBuilder() {
        return new BaseFilterConfigBuilder<Filter_>();
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().addColumnSortHandler(this));
        registerHandler(getView().addSelectionChangeHandler(this));

        provider.addDataDisplay(getView().getGridDataDisplay());

        onSelectionChanged(getSelectedObject());
    }




    @Override
    protected void onUnbind() {
        provider.removeDataDisplay(getView().getGridDataDisplay());
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        update();

        getView().setupRoles(currentUser.getRoles());
        getView().updateToolbar();
    }

    protected void update() {
        loader.load();
    }



    @Nullable
    public T getSelectedObject() {
        final T selectedObject = getView().getSelectedObject();

        if (selectedObject != null) {
            final Object key = getView().getKey(selectedObject);
            return provider.findModel(key);
        }

        return null;
    }

    @Override
    public void onPerformFilter(Filter_ filter) {
        provider.setFilter(filter);

        getView().firstPage();

        loader.load();
    }

    @Override
    public void onColumnSort(ColumnSortEvent event) {
        provider.setSorting(event.getColumnSortList());
        HasData<T> hasData = getView().getGridDataDisplay();
        hasData.setVisibleRangeAndClearData(hasData.getVisibleRange(), true);
    }

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        onSelectionChanged(getSelectedObject());
    }

    @Override
    public void onView(T selectedObject) {

    }

    @Override
    public void onEdit(T selectedObject) {

    }

    @Override
    public void onRemove(T selectedObject) {

    }

    @Override
    public void onCreate() {

    }
}
