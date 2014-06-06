/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.events.StartTypingEvent;
import com.ifree.common.gwt.client.rest.ListingRestService;
import com.ifree.common.gwt.client.ui.application.CountBackAsyncCallback;
import com.ifree.common.gwt.client.ui.constants.BaseNameTokes;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.client.ui.grids.BaseFilterConfigBuilder;
import com.ifree.common.gwt.client.ui.grids.PagingSortingFilteringDataProvider;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.application.security.CurrentUser;
import com.ifree.common.gwt.client.ui.grids.AbstractFilterHandler;
import com.ifree.common.gwt.client.utils.ViewHeaderResolver;
import com.ifree.common.gwt.shared.loader.FilterPagingLoadConfig;
import com.ifree.common.gwt.shared.loader.FilterPagingLoader;
import com.ifree.common.gwt.shared.loader.PagingLoadResult;
import com.ifree.common.gwt.shared.loader.PagingLoader;

import javax.annotation.Nullable;
import java.util.List;


/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.07.13
 */
@SuppressWarnings("TypeParameterNamingConvention")
public abstract class BaseListPresenter<T,
                                        Filter_ extends BaseFilter,
                                        View_ extends ListView<T, Filter_>,
                                        Proxy_ extends ProxyPlace<?>,
                                        Service_ extends ListingRestService<T>
                                        >
        extends Presenter<View_, Proxy_>
        implements ColumnSortEvent.Handler,
        SelectionChangeEvent.Handler, ListUiHandler<T, Filter_ >, PerformFilterEvent.PerformFilterHandler,
        StartTypingEvent.StartTypingHandler {

    @Inject
    protected PlaceManager placeManager;

    @Inject
    private CurrentUser currentUser;

    @Inject
    protected RestDispatch restDispatch;

    @Inject
    protected ViewHeaderResolver headerResolver;

    protected final Service_ listService;

    protected final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader;
    protected final PagingSortingFilteringDataProvider<T, Filter_> provider;

    private List<Action<T>> actionList = Lists.newArrayList();


    protected BaseListPresenter(EventBus eventBus, View_ view, Proxy_ proxy,
                                GwtEvent.Type<RevealContentHandler<?>> slot,
                                BaseDataProxy<T> dataProxy,
                                Service_ listService) {
        super(eventBus, view, proxy, slot);
        this.listService = listService;
        loader = new FilterPagingLoader(dataProxy);
        provider = createProvider(view);


    }

    private void initAction() {
        addActions(createActions());
    }

    protected abstract Action [] createActions();

    private PagingSortingFilteringDataProvider<T, Filter_> createProvider(View_ view) {
        return new PagingSortingFilteringDataProvider<T, Filter_>(loader, view, createFilterConfigBuilder());
    }

    protected abstract BaseFilterConfigBuilder<Filter_> createFilterConfigBuilder();

    @Override
    protected void onBind() {
        super.onBind();

        initAction();

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

        setupFilter();

        refresh();

        getView().setupRoles(currentUser.getRoles());
        getView().updateToolbar();

        getView().updateHeader(getDisplayHeader());

        updateActions();

    }

    private void setupFilter() {
        PlaceRequest request = placeManager.getCurrentPlaceRequest();
        String filter = request.getParameter(BaseNameTokes.FILTER, null);
        AbstractFilterHandler<Filter_> filterHandler = getFilterHandler();
        if (filterHandler != null && filter != null) {
            Filter_ filter_ = filterHandler.convertToObject(filter);
            provider.setFilter(filter_);
            getView().setFilter(filter_);
        }

    }

    private String getDisplayHeader() {
        Proxy_ proxy = getProxy();
        String nameToken = proxy.getNameToken();
        return headerResolver.resolve(nameToken);

    }

    protected void refresh() {
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

    @ProxyEvent
    public void onStartTyping(StartTypingEvent event) {
        Character symbol = event.getSymbol();
        getView().focusFilter(symbol);
    }




    @Override
    public void onPerformFilter(PerformFilterEvent filterEvent) {
        Filter_ filter = (Filter_) filterEvent.getFilter();
        provider.setFilter(filter);

        getView().firstPage();

        loader.load();

        AbstractFilterHandler<Filter_> filterHandler = getFilterHandler();
        if (filterHandler != null) {
            PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();

            PlaceRequest.Builder builder = new PlaceRequest.Builder()
                    .nameToken(currentPlaceRequest.getNameToken());
            if (filter != null) {
                String filterString = filterHandler.convertToString(filter);
                builder.with(BaseNameTokes.FILTER, filterString).build();
            }

            placeManager.updateHistory(builder.build(), true);
        }

    }


    protected CountBackAsyncCallback createManualRevealingCallback() {
        return new CountBackAsyncCallback(getProxy(), this);
    }

    public abstract AbstractFilterHandler<Filter_> getFilterHandler();

    @Override
    public void onColumnSort(ColumnSortEvent event) {
        provider.setSorting(event.getColumnSortList());
        HasData<T> hasData = getView().getGridDataDisplay();
        hasData.setVisibleRangeAndClearData(hasData.getVisibleRange(), true);
    }

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        T selectedObject = getSelectedObject();
        onSelectionChanged(selectedObject);

        updateActions();
    }

    private void updateActions() {
        T selectedObject;
        selectedObject = getSelectedObject();

        for (Action<T> action : actionList) {
            String displayText = action.getDisplayText(selectedObject);
            getView().updateAction(action,
                    action.isEnabled(selectedObject),
                    action.isVisible(selectedObject),
                    displayText);
        }
    }

    public void addActions(Action<T>... actions) {
        if (actions != null) {
            for (final Action<T> action : actions) {
                actionList.add(action);
                getView().addAction(action, new Command() {

                    @Override
                    public void execute() {
                        action.perform(getSelectedObject());
                    }
                });
            }
        }
    }

    @Override
    public void onSelectionChanged(T selection) {

    }

    public Service_ getService() {
        return listService;
    }
}
