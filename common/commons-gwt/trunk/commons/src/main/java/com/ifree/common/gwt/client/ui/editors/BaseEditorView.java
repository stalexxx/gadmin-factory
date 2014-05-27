/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.editors;

import com.google.common.collect.Maps;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.impl.DelegateMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.ifree.common.gwt.client.ui.validation.DefaultEditorError;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 26.04.13
 */
public abstract class BaseEditorView<T, C extends BaseEditorUiHandlers, E extends Editor<T>> extends ViewWithUiHandlers<C> implements EditorView<T> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @UiField
    public BaseEditorLayout layout;
    @UiField
    public Button save;
    private Widget[] validationFields;

    private Map<Editor, FormGroup> groupMap = Maps.newHashMap();

    private final SimpleBeanEditorDriver<T, E> driver;

    protected BaseEditorView(SimpleBeanEditorDriver<T, E> driver) {
        this.driver = driver;
    }


    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void initWidget(Widget widget) {
        super.initWidget(widget);

        initFieldvalidation();
    }

    private void initFieldvalidation() {
        final Widget[] widgets = validationFields();
        for (Widget widget : widgets) {
            final Widget parent = widget.getParent();
            if (parent instanceof FormGroup) {
                FormGroup formGroup = (FormGroup) parent;
                if (widget instanceof IsEditor) {
                    IsEditor isEditor = (IsEditor) widget;
                    groupMap.put(isEditor.asEditor(), formGroup);
                } else if (widget instanceof Editor) {
                    groupMap.put((Editor) widget, formGroup);
                }
            }


            ///add change handler
            if (widget instanceof HasValueChangeHandlers) {
                ((HasValue) widget).addValueChangeHandler(new ValueChangeHandler() {
                    @Override
                    public void onValueChange(ValueChangeEvent event) {
                        getUiHandlers().validate();
                    }
                });
            }
        }
    }


    @Override
    public SimpleBeanEditorDriver<T, E> getDriver() {
        return driver;
    }

    @UiHandler("save")
    public final void onSave(ClickEvent event) {
        getUiHandlers().onSave();
    }

    protected abstract Widget[] validationFields();

    @Override
    public void setUiHandlers(C uiHandlers) {
        super.setUiHandlers(uiHandlers);

        layout.setUiHandlers(uiHandlers);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void clearValidation() {
        showErrors(null);

    }

    /**
     * @param editMode true if edit, false if create
     */
    @Override
    public void setEditableMode(boolean editMode) {

    }


    public void handleViolations(Set<ConstraintViolation<T>> violations) {

        List<EditorError> errors = getEditorErrors(violations, getDriver());

        showErrors(errors);

    }

    @Override
    public void initializeDriver() {
        getDriver().initialize((E) this);
    }

    private List<EditorError> getEditorErrors(Set<ConstraintViolation<T>> violations, SimpleBeanEditorDriver<T, ?> driver) {
        DelegateMap delegateMap = DelegateMap.of(driver, DelegateMap.IDENTITY);

        ArrayList<EditorError> errors = new ArrayList<EditorError>(violations.size());

        if (violations != null) {
            for (ConstraintViolation<T> violation : violations) {
                final List<Editor<?>> editorsByPath = delegateMap.getEditorByPath(violation.getPropertyPath().toString());

                for (Editor<?> editor : editorsByPath) {
                    errors.add(new DefaultEditorError(editor, violation.getMessage(), violation.getInvalidValue()));
                }
            }
        }

        return errors;
    }
    @Override
    public void showErrors(List<EditorError> errors) {

        for (FormGroup formGroup : groupMap.values()) {
            formGroup.setValidationState(ValidationState.SUCCESS);
        }

        if (errors != null) {
            for (EditorError error : errors) {
                Editor<?> editor = error.getEditor();

                final FormGroup group = groupMap.get(editor);

                if (group != null) {
                    group.setValidationState(ValidationState.ERROR);
                }
            }
        }
    }


    @Override
    public void setupRoles(List<String> roles) {

    }


}
