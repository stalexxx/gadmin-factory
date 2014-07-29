package com.ifree.common.gwt.client.utils;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.constants.BaseNameTokes;
import com.ifree.common.gwt.client.ui.grids.AbstractFilterHandler;

/**
 * Created by alex on 15.05.14.
 */
public class PlaceManagerHelper {
    public static  <F extends BaseFilter> void revealFilter(F filter, AbstractFilterHandler<F> handler,
                                                            PlaceManager placeManager, String placeToken) {
        PlaceRequest.Builder builder = new PlaceRequest.Builder();
        builder.nameToken(placeToken);

        String filterString = handler.convertToString(filter);
        builder.with(BaseNameTokes.FILTER, filterString).build();

        placeManager.revealPlace(builder.build());
    }
}
