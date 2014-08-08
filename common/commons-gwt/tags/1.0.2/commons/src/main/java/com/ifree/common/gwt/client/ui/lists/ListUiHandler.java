/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.lists;

import com.gwtplatform.mvp.client.UiHandlers;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.ui.application.Filter;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 13.09.13
 */
public interface ListUiHandler<T, F extends Filter> extends UiHandlers, PerformFilterEvent.PerformFilterHandler{

    /*===========================================[ CLASS METHODS ]================*/

    void onSelectionChanged(T selection);


//    CRUD

    /*void onView(T selectedObject);

    void onEdit(T selectedObject);

    void onRemove(T selectedObject);

    void onCreate();*/
}
