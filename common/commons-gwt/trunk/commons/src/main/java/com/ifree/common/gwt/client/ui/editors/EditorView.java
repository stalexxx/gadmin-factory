/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.gwtplatform.mvp.client.View;
import com.ifree.common.gwt.client.ui.application.CustomizedWithRoles;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 25.04.13
 */
public interface EditorView<T> extends View, HasEditorErrors<T>, CustomizedWithRoles {

    /*===========================================[ INTERFACE METHODS ]============*/

    SimpleBeanEditorDriver<T, ?> getDriver();

    void clearValidation();

    /**
     * @param editMode true if edit, false if create
     */
    void setEditableMode(boolean editMode);

    void handleViolations(Set<ConstraintViolation<T>> violations);

    void initializeDriver();

    void setSaveButtonEnabled(boolean enabled);
}
