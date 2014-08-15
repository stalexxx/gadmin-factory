package com.ifree.common.gwt.client.actions.generic;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestAction;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.ifree.common.gwt.client.actions.SingleItemAlwaysVisibleAction;
import com.ifree.common.gwt.client.events.ShowAlertEvent;
import com.ifree.common.gwt.client.ui.AbstractAsyncCallback;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.ConfirmCallback;

import javax.annotation.Nonnull;

/**
* Created by alex on 28.04.14.
*/
public abstract class GenericRemoveAction<T, ID> extends SingleItemAlwaysVisibleAction<T> {

    private ValueProvider<T, ID> idProvider;
    private RestDispatch dispatch;
    private EventBus eventBus;

    public GenericRemoveAction(ValueProvider<T, ID> idProvider, RestDispatch dispatch, EventBus eventBus) {
        super("Удалить");
        this.idProvider = idProvider;
        this.dispatch = dispatch;
        this.eventBus = eventBus;
        setIconType(IconType.TRASH_O);
    }

    @Override
    protected void nonNullPerform(final @Nonnull T item) {
        Bootbox.confirm("Удалить?", new ConfirmCallback() {
            @Override
            public void callback(boolean b) {
                if (b) {
                    ID id = getId(item);
                    dispatch.execute(createAction(id), new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            eventBus.fireEvent(new ShowAlertEvent("Успешно удален", AlertType.SUCCESS));
                            onDeleted(item);
                        }
                    });

                }
            }
        });

    }

    protected abstract void onDeleted(T item);

    protected abstract RestAction<Boolean> createAction(ID id);

    protected ID getId(T item) {
        return item != null ? idProvider.getValue(item) : null;
    }
}
