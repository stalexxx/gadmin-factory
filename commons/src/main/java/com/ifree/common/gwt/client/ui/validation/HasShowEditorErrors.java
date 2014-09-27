/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.validation;

import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 18.04.13
 */
public interface HasShowEditorErrors<T> extends HasEditorErrors<T>, IsWidget {
    /**
     * The widget that will be decorated on <code>EditorError</code>s will be added de <code>FormGroupType.ERROR</code> style.
     * It can be a FormGroup or any widget.
     *
     * @param controlGroup
     */
    void setFormGroup(Widget controlGroup);

    /**
     * Widget where <code>EditorError</code>s messages will be placed.
     * It can be a HelpBlock or any other widget.
     *
     * @param errorLabel
     */
    void setErrorLabel(Widget errorLabel);

    /**
     * Sets the content of the <code>EditorError</code>s messages inside de <code>errorLabel</code>.
     * This implementation uses {@link com.google.gwt.dom.client.Element#setInnerHTML(String)} to set the content.
     *
     * @param errorMessage
     */
    void setErrorLabelText(String errorMessage);

}
