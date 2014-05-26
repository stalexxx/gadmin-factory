package com.ifree.common.gwt.client.actions.generic;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.actions.SimpleAction;

import javax.annotation.Nullable;

/**
* Created by alex on 28.04.14.
*/
public class GenericCreateAction<T> extends SimpleAction<T> {

    private PlaceManager placeManager;
    private final String token;

    public GenericCreateAction(PlaceManager placeManager, String token) {
        super("Создать");
        this.placeManager = placeManager;
        this.token = token;
    }

    @Override
    public void perform(@Nullable Object item) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(token).build();
        placeManager.revealPlace(placeRequest);
    }
}
