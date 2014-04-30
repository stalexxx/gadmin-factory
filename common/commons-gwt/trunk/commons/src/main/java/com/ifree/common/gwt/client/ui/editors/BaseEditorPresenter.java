/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.validation.client.impl.Validation;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.events.ShowAlertEvent;
import com.ifree.common.gwt.client.rest.CRUDRestService;
import com.ifree.common.gwt.client.ui.application.AlertingAsyncCallback;
import com.ifree.common.gwt.client.ui.application.BlockingOverlay;
import com.ifree.common.gwt.client.ui.application.security.CurrentUser;
import com.ifree.common.gwt.client.ui.constants.BaseMessages;
import com.ifree.common.gwt.client.ui.constants.BaseNameTokes;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 25.03.13
 */

@SuppressWarnings("TypeParameterNamingConvention")
public abstract class BaseEditorPresenter<
        T, ID extends Serializable,
        View_ extends EditorView<T> & HasUiHandlers<?>,
        Proxy_ extends ProxyPlace<?>,
        RestService_ extends CRUDRestService<T, ID>
        >
        extends Presenter<View_, Proxy_> implements BaseEditorUiHandlers {

/*===========================================[ INSTANCE VARIABLES ]===========*/





    protected final PlaceManager placeManager;

    protected RestService_ service;

    protected T currentDto;

    @Inject
    protected BlockingOverlay blockingOverlay;

    @Inject
    protected RestDispatch dispatcher;

    @Inject
    protected BaseMessages messages;

    @Inject
    private CurrentUser currentUser;


    protected final Logger logger;

    private ID id;

/*===========================================[ CONSTRUCTORS ]=================*/


    protected BaseEditorPresenter(EventBus eventBus, View_ view, Proxy_ proxy, PlaceManager placeManager,
                                  GwtEvent.Type<RevealContentHandler<?>> slot, Logger logger, RestService_ service) {
        super(eventBus, view, proxy, slot);

        this.placeManager = placeManager;

        this.logger = logger;
        this.service = service;

    }

    @Override
    protected void onBind() {
        super.onBind();

        getView().initializeDriver();
    }


    /*===========================================[ CLASS METHODS ]================*/



    @Override
    protected void onReveal() {
        super.onReveal();

        getView().setupRoles(currentUser.getRoles());

        load(id);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {

        String parameter = request.getParameter(BaseNameTokes.ID_PARAM, null);
        id = convertId(parameter);

        super.prepareFromRequest(request);
    }

    @Nullable
    protected abstract ID convertId(String parameter);


    protected void load(ID id) {
        if (id != null) {
            find(id, new AlertingAsyncCallback<T>(getEventBus()) {
                @Override
                public void onSuccess(T result) {
                    switchEditableMode(true);
                    loadDto(result);

                }
            });
        } else {
            switchEditableMode(true);
            currentDto = create(service);

            preEditableLoaded();
            getView().getDriver().edit(currentDto);
            onEditableLoaded(currentDto);
        }
    }

    private void switchEditableMode(boolean editable) {
        getView().setEditableMode(editable);
    }


    protected abstract void find(ID id, AsyncCallback<T> receiver);

    protected abstract T create(RestService_ currentContext);

/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void validate() {
        final T dto = getView().getDriver().flush();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = doValidate(dto, validator);

        if (!violations.isEmpty()) {
           getView().handleViolations(violations);
        }
    }

    @Override
    public void onSave() {
        final T dto = getView().getDriver().flush();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<T>> violations = doValidate(dto, validator);

        if (!violations.isEmpty()) {

            /*for (ConstraintViolation<T> violation : violations) {
                getEventBus().fireEvent(new ShowAlertEvent(violation.getPropertyPath() + "  " +violation.getMessage(), AlertType.DANGER));


            }*/
            getView().handleViolations(violations);


        } else {
            RestAction<ID> action;
            if (getId(dto) == null) {
                action = service.create(dto);
            } else {
                action = service.update(dto);
            }


            dispatcher.execute(action, new AlertingAsyncCallback<ID>(getEventBus()) {
                @Override
                public void onSuccess(ID result) {
                    onBack();
                }
            });
        }

/*
        RequestContext flush = getView().getDriver().flush();
        flush.fire(new Receiver<Void>() {

            @Override
            public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                //getView().getDriver().setConstraintViolations(violations);
                List<EditorError> errors = ValidationUtils.getEditorErrors(violations, getView().getDriver());
                getView().showErrors(errors);
            }


            @Override
            public void onSuccess(Void response) {
                List<EditorError> errors = makeCustomValidation(currentDto);

                if (!errors.isEmpty()) {
                    load(currentDto);

                    getView().showErrors(errors);
                } else {
                    if (getId(currentDto) == null) {
                        createContext().create(currentDto).fire(new BlockingReciever<Void>(blockingOverlay) {

                            @Override
                            public void onActionFailure(ServerFailure error) {
                                load(currentDto);
                                onShowFailure(error);
                            }


                            @Override
                            public void onActionSuccess(Void response) {
                                alertsPanel.addAlert(messages.entityCreated(getEntityDisplayName()), AlertType.SUCCESS);
                                load(currentDto);

                                onBack();
                            }
                        });
                    } else {
                        createContext().update(currentDto).fire(new BlockingReciever<Void>(blockingOverlay) {
                            @Override
                            public void onActionFailure(ServerFailure error) {
                                load(currentDto);
                                onShowFailure(error);
                            }


                            @Override
                            public void onActionSuccess(Void response) {
                                alertsPanel.addAlert(messages.entityUpdated(), AlertType.SUCCESS);
                                load(currentDto);

                                onBack();
                            }
                        });
                    }
                }

            }
        });
*/
    }

    private Set<ConstraintViolation<T>> validate(T dto) {
       // Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return doValidate(dto, null);


    }

    protected abstract Set<ConstraintViolation<T>> doValidate(T dto, Validator validator);

    protected abstract RestAction<ID> getSaveOrCreateAction(T dto);

    protected List<EditorError> makeCustomValidation(T currentProxy) {
        return Lists.newArrayList();
    }

    protected abstract String getEntityDisplayName();

    protected abstract Object getId(T currentProxy);

    protected void loadDto(T proxy) {
        /*service = createContext();
        currentDto = service.edit(proxy);
*/

        preEditableLoaded();
        getView().getDriver().edit(proxy);

        onEditableLoaded(proxy);
    }

    protected void preEditableLoaded() {


    }


    protected void onEditableLoaded(T proxy) {
        getView().clearValidation();
    }

    /*protected void onShowFailure(ServerFailure error) {
        *//*if (ValidationUtils.isValidationException(error)) {
            alertsPanel.addAlert(messages.validationError(error.getMessage()), AlertType.ERROR);
        }*//*
    }*/

    @Override
    public void onBack() {
        PlaceRequest.Builder builder = new PlaceRequest.Builder();

        placeManager.revealPlace(builder.nameToken(getListPlace()).build());
    }

    protected abstract String getListPlace();

    public Logger getLogger() {
        return logger;
    }

    @Override
    public void alert(String message, AlertType type) {
        getEventBus().fireEvent(new ShowAlertEvent(message, type));
    }
}
