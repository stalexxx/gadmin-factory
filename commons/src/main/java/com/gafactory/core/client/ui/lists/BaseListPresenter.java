/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.lists;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
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
import com.gafactory.core.client.actions.Action;
import com.gafactory.core.client.events.PerformFilterEvent;
import com.gafactory.core.client.events.ShowAlertEvent;
import com.gafactory.core.client.events.StartTypingEvent;
import com.gafactory.core.client.rest.ListingRestService;
import com.gafactory.core.client.ui.BaseFilter;
import com.gafactory.core.client.ui.application.AlertingAsyncCallback;
import com.gafactory.core.client.ui.application.CountBackAsyncCallback;
import com.gafactory.core.client.ui.application.security.CurrentUser;
import com.gafactory.core.client.ui.constants.BaseNameTokes;
import com.gafactory.core.client.ui.grids.AbstractFilterHandler;
import com.gafactory.core.client.ui.grids.BaseDataProxy;
import com.gafactory.core.client.ui.grids.BaseFilterHelper;
import com.gafactory.core.client.utils.ViewHeaderResolver;
import com.gafactory.core.shared.PropertyAccess;
import com.gafactory.core.shared.loader.FilterPagingLoadConfig;
import com.gafactory.core.shared.loader.LoadEvent;
import com.gafactory.core.shared.loader.LoadHandler;
import com.gafactory.core.shared.loader.PagingLoadResult;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.07.13
 */
@SuppressWarnings("TypeParameterNamingConvention")
public abstract class BaseListPresenter<T,
        Filter_ extends BaseFilter,
        View_ extends ListView<T, Filter_>,
        Proxy_ extends ProxyPlace<?>,
        Service_ extends ListingRestService<T>,
        Properties_ extends PropertyAccess<T>
        >
        extends Presenter<View_, Proxy_>
        implements
        SelectionChangeEvent.Handler, ListUiHandler<T, Filter_>, PerformFilterEvent.PerformFilterHandler,
        StartTypingEvent.StartTypingHandler, LoadHandler<FilterPagingLoadConfig, PagingLoadResult<T>> {


    @Inject
    protected PlaceManager placeManager;

    @Inject
    private CurrentUser currentUser;

    @Inject
    protected RestDispatch restDispatch;

    @Inject
    protected ViewHeaderResolver headerResolver;

    @Inject
    protected BaseFilterHelper filterHelper;

    protected final Service_ listService;

    protected final Properties_ properties;

    private List<Action<T>> actionList = Lists.newArrayList();
    private String lastFilterString;

    protected BaseListPresenter(EventBus eventBus, View_ view, Proxy_ proxy,
                                GwtEvent.Type<RevealContentHandler<?>> slot,
                                BaseDataProxy<T> dataProxy,
                                Service_ listService, Properties_ properties) {
        super(eventBus, view, proxy, slot);
        this.listService = listService;
        this.properties = properties;


    }

    private void initAction() {
        addActions(createActions());
    }


    protected abstract List<Action<T>> createActions();

    @Override
    protected void onBind() {
        super.onBind();

        initAction();
        getView().postConstruct();

        registerHandler(getView().addSelectionChangeHandler(this));
        registerHandler(getView().addLoadHandler(this));
        getView().getGrid().setFilterConfigBuilder(getFilterHandler().createConfigBuilder());
        onSelectionChanged(getSelectedObject());
    }


    @Override
    protected void onReveal() {
        super.onReveal();
        getView().setupRoles(currentUser.getRoles());
        getView().updateToolbar();
        getView().updateHeader(getDisplayHeader());

        getView().scrollToSelected();
    }

    @Override
    protected void onReset() {
        setupFilter();
        refresh();
        updateActions();
    }

    /**
     *
     * @return if filterChanged
     */
    private void setupFilter() {
        PlaceRequest request = placeManager.getCurrentPlaceRequest();
        String filter = request.getParameter(BaseNameTokes.FILTER, null);

        if (!Objects.equals(lastFilterString, filter)) {
            AbstractFilterHandler<Filter_> filterHandler = getFilterHandler();
            if (filterHandler != null && filter != null && !filter.isEmpty()) {
                Filter_ filter_ = filterHandler.convertToObject(filter);
                getView().setFilter(filter_);
            } else {
                getView().clearFilter();
            }

            lastFilterString = filter;
        }

    }

    private String getDisplayHeader() {
        Proxy_ proxy = getProxy();
        String nameToken = proxy.getNameToken();
        return headerResolver.resolve(nameToken);

    }

    protected void refresh() {
        getView().getGrid().reload();
    }


    @Nullable
    public T getSelectedObject() {
        final T selectedObject = getView().getSelectedObject();

        if (selectedObject != null) {
            final Object key = getView().getKey(selectedObject);
            return getView().getGrid().getDataPrivider().findModel(key);
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

        getView().setFilter(filter);

        getView().firstPage();

        refresh();

        AbstractFilterHandler<Filter_> filterHandler = getFilterHandler();
        if (filterHandler != null) {
            PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();

            PlaceRequest.Builder builder = new PlaceRequest.Builder()
                    .nameToken(currentPlaceRequest.getNameToken());
            if (filter != null) {
                String filterString = filterHandler.convertToString(filter);
                builder.with(BaseNameTokes.FILTER, filterString).build();
                lastFilterString = filterString;
            }

            placeManager.updateHistory(builder.build(), true);
        }

    }


    protected CountBackAsyncCallback createManualRevealingCallback() {
        return new CountBackAsyncCallback(getProxy(), this);
    }

    public abstract AbstractFilterHandler<Filter_> getFilterHandler();


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
            IconType displayIcon = action.getDisplayIcon();

            getView().updateAction(action,
                    action.isEnabled(selectedObject),
                    action.isVisible(selectedObject),
                    displayText, displayIcon);
        }
    }

    public void addActions(List<Action<T>> actions) {
        if (actions != null) {

            for (final Action<T> action : actions) {
                actionList.add(action);

                final HasClickHandlers actionWidget = getView().addAction(action);

                if (action.getType().equals(Action.ACTION_TYPE.SCRIPT)) {
                    registerHandler(actionWidget.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            if (action.isEnabled(getSelectedObject())) {
                                action.perform(getSelectedObject());
                            }
                        }
                    }));
                }

            }
        }
    }

    @Override
    public void onSelectionChanged(T selection) {

    }

    public Service_ getService() {
        return listService;
    }

    @Override
    public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<T>> event) {
        //мы должны быть уверены что updateActions будет вызвано ПОСЛЕ того как все желающие получили LoadEvent
        // потому что именно его слушает провайдер
        updateActions();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
            }
        });
    }

    protected class RefreshingCallback extends AlertingAsyncCallback<Void> {

        private final String message;

        public RefreshingCallback(String message) {
            super(getEventBus());
            this.message = message;
        }

        @Override
        public void onSuccess(Void result) {
            getEventBus().fireEvent(new ShowAlertEvent(message, AlertType.SUCCESS));

            refresh();
        }
    }

    public void alert(String message, AlertType type) {
        getEventBus().fireEvent(new ShowAlertEvent(message, type));
    }
}
