/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.gwtplatform.mvp.client.UiHandlers;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 25.03.13
 */
public interface BaseEditorUiHandlers extends UiHandlers {

    /*===========================================[ INTERFACE METHODS ]============*/

    void onBack();

    void onSave();

    void validate();

    void alert(String message, AlertType type);
}
