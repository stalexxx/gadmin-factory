package com.gafactory.core.client.actions.generic;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gafactory.core.client.actions.SinglePlaceRequestAction;
import com.gafactory.core.client.ui.constants.BaseNameTokes;
import com.gafactory.core.shared.ModelKeyProvider;
import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nonnull;

/**
* Created by alex on 28.04.14.
*/
public class GenericEditAction<T> extends SinglePlaceRequestAction<T> {

    private final String token;
    private ModelKeyProvider<T> keyProvider;

    public GenericEditAction(PlaceManager placeManager, String token, ModelKeyProvider<T> keyProvider, String caption) {
        super(caption, placeManager);
        this.token = token;
        this.keyProvider = keyProvider;
        setIconType(IconType.PENCIL);
    }

    public GenericEditAction(PlaceManager placeManager, String token, ModelKeyProvider<T> keyProvider) {
        this(placeManager, token, keyProvider, "Редактировать");
    }


    @Override
    protected PlaceRequest buildToken(@Nonnull T item) {
        return new PlaceRequest.Builder().nameToken(token).
                with(BaseNameTokes.ID_PARAM, getId(item)).build();

    }

    protected String getId(T item) {
        return item != null ? keyProvider.getKey(item) : null;
    }
}
