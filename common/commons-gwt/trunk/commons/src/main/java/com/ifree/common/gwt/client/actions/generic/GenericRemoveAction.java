package com.ifree.common.gwt.client.actions.generic;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.actions.SingleItemAlwaysVisibleAction;
import com.ifree.common.gwt.client.events.ShowAlertEvent;
import com.ifree.common.gwt.client.rest.CRUDRestService;
import com.ifree.common.gwt.client.ui.AbstractAsyncCallback;
import com.ifree.common.gwt.client.ui.constants.BaseNameTokes;
import com.ifree.common.gwt.shared.ModelKeyProvider;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;
import org.gwtbootstrap3.extras.bootbox.client.callback.ConfirmCallback;

import javax.annotation.Nonnull;

/**
* Created by alex on 28.04.14.
*/
public abstract class GenericRemoveAction<T, ID> extends SingleItemAlwaysVisibleAction<T> {

    private ModelKeyProvider<T> keyProvider;
    private RestDispatch dispatch;
    private EventBus eventBus;

    public GenericRemoveAction(ModelKeyProvider<T> keyProvider, RestDispatch dispatch, EventBus eventBus) {
        super("Удалить");
        this.keyProvider = keyProvider;
        this.dispatch = dispatch;
        this.eventBus = eventBus;
    }

    @Override
    protected void nonNullPerform(final @Nonnull T item) {
        Bootbox.confirm("Удалить?", new ConfirmCallback() {
            @Override
            public void callback(boolean b) {
                if (b) {
                    String id = getId(item);
                    dispatch.execute(createAction(id), new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
//                        eventBus.fireEvent(new ShowAlertEvent(""));
                            onDeleted(item);

                        }
                    });

                }
            }
        });

    }

    protected abstract void onDeleted(T item);

    protected abstract RestAction<Boolean> createAction(String id);

    protected String getId(T item) {
        return item != null ? keyProvider.getKey(item) : null;
    }
}
