/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.lists;

import com.gwtplatform.mvp.client.UiHandlers;
import com.gafactory.core.client.events.PerformFilterEvent;
import com.gafactory.core.client.ui.application.Filter;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 13.09.13
 */
public interface ListUiHandler<T, F extends Filter> extends UiHandlers, PerformFilterEvent.PerformFilterHandler{

    /*===========================================[ CLASS METHODS ]================*/

    void onSelectionChanged(T selection);

    T getSelectedObject();

//    CRUD

    /*void onView(T selectedObject);

    void onEdit(T selectedObject);

    void onRemove(T selectedObject);

    void onCreate();*/
}
