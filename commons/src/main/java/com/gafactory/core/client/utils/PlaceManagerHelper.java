package com.gafactory.core.client.utils;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gafactory.core.client.ui.BaseFilter;
import com.gafactory.core.client.ui.constants.BaseNameTokes;
import com.gafactory.core.client.ui.grids.AbstractFilterHandler;

/**
 * Created by alex on 15.05.14.
 */
public class PlaceManagerHelper {
    public static  <F extends BaseFilter> void revealFilter(F filter, AbstractFilterHandler<F> handler,
                                                            PlaceManager placeManager, String placeToken) {

        placeManager.revealPlace(buildFilterRequest(filter, handler, placeToken));
    }


    public static <F extends BaseFilter> PlaceRequest buildFilterRequest(F filter, AbstractFilterHandler<F> handler,
                                                                           String placeToken) {
        PlaceRequest.Builder builder = new PlaceRequest.Builder();
        builder.nameToken(placeToken);

        String filterString = handler.convertToString(filter);
        builder.with(BaseNameTokes.FILTER, filterString).build();

        return builder.build();

    }
}
