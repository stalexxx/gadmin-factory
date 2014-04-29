package com.ifree.common.gwt.client.actions.generic;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.actions.SingleItemAlwaysVisibleAction;
import com.ifree.common.gwt.client.ui.constants.BaseNameTokes;
import com.ifree.common.gwt.shared.ModelKeyProvider;

/**
* Created by alex on 28.04.14.
*/
public class GenericEditAction<T> extends SingleItemAlwaysVisibleAction<T> {

    private PlaceManager placeManager;
    private final String token;
    private ModelKeyProvider<T> keyProvider;

    public GenericEditAction(PlaceManager placeManager, String token, ModelKeyProvider<T> keyProvider) {
        super("Редактировать");
        this.placeManager = placeManager;
        this.token = token;
        this.keyProvider = keyProvider;
    }

    @Override
    protected void nonNullPerform(T item) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(token).
                with(BaseNameTokes.ID_PARAM, getId(item)).build();
        placeManager.revealPlace(placeRequest);

    }


    protected String getId(T item) {
        return item != null ? keyProvider.getKey(item) : null;
    }
}
