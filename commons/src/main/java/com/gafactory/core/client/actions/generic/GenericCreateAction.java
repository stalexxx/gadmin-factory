package com.gafactory.core.client.actions.generic;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gafactory.core.client.actions.SimpleAction;
import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;

/**
* Created by alex on 28.04.14.
*/
public class GenericCreateAction<T> extends SimpleAction<T> {

    private PlaceManager placeManager;
    private final String token;

    public GenericCreateAction(PlaceManager placeManager, String token) {
        this(placeManager, token, "Создать");
    }

    public GenericCreateAction(PlaceManager placeManager, String token, String caption) {
        super(caption);
        this.placeManager = placeManager;
        this.token = token;

        setIconType(IconType.FILE_TEXT_O);
    }

    @Override
    public void perform(@Nullable Object item) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(token).build();
        placeManager.revealPlace(placeRequest);
    }
}
