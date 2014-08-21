package com.ifree.common.gwt.client.actions;

import com.google.common.base.Function;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.grids.AbstractFilterHandler;
import com.ifree.common.gwt.client.utils.PlaceManagerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by alex on 04.08.14.
 */
public abstract class SinglePlaceRequestAction<T> extends SingleItemAlwaysVisibleAction<T> {
    private PlaceManager placeManager;

    public SinglePlaceRequestAction(String text, PlaceManager placeManager) {
        super(text);
        this.placeManager = placeManager;
    }


    @Override
    public ACTION_TYPE getType() {
        return ACTION_TYPE.HISTORY_TOKEN;
    }

    @Override
    public String actualHistoryTokenOrLink(@Nullable T item) {
        if (item != null) {
            PlaceRequest placeRequest = buildToken(item);
            return placeRequest != null ? placeManager.buildHistoryToken(placeRequest) : null;
        }
        return null;
    }

    protected abstract PlaceRequest buildToken(@Nonnull T item);

    @Override
    protected final void nonNullPerform(@Nonnull T item) {

    }

    public <F extends BaseFilter> PlaceRequest revealFilter(F filter, AbstractFilterHandler<F> handler, String placeToken) {
        return PlaceManagerHelper.buildFilterRequest(filter, handler, placeToken);

    }

    public static <T, F extends BaseFilter> SinglePlaceRequestAction<T> filterRequestAction(String text, PlaceManager placeManager, final Function<T, F> filterProvider,
                                                                                      final AbstractFilterHandler<F> handler, final String placeToken) {
        return new SinglePlaceRequestAction<T>(text, placeManager) {
            @Override
            protected PlaceRequest buildToken(@Nonnull T item) {
                return PlaceManagerHelper.buildFilterRequest(filterProvider.apply(item), handler, placeToken);
            }
        };
    }
}
