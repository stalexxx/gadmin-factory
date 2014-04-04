/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.HasEditorErrors;
import com.gwtplatform.mvp.client.PopupView;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 25.04.13
 */
public interface PopupEditorView<T> extends PopupView, HasEditorErrors<T> {

    /*===========================================[ INTERFACE METHODS ]============*/

    EditorDriver<T> getDriver();

    void clearValidation();

}
