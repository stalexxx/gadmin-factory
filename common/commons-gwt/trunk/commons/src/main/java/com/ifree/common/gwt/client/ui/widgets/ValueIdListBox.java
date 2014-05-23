/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.ifree.common.gwt.shared.ValueProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 16.07.13
 */
public class ValueIdListBox<T, ID> extends Composite implements LeafValueEditor<ID>, HasValue<ID>,
        SelectionChangeEvent.HasSelectionChangedHandlers, /*HasShowEditorErrors<ID>,*/ HasEnabled {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    /**
     * Widget for control decoration on <code>EditorError</code>s
     */
    private ValueListBox<T> listBox;

    private Function<T, ID> idProvider;
    private ID value;
    private List<T> acceptableValues;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public ValueIdListBox(final Renderer<T> renderer, final ValueProvider<T, ID> valueProvider) {
        assert renderer != null;

        this.idProvider = new Function<T, ID>() {
            @Nullable
            @Override
            public ID apply(@Nullable T input) {
                return input != null ? valueProvider.getValue(input) : null;
            }
        };

        listBox = new ValueListBox<T>(renderer, new ProvidesKey<T>() {
            @Override
            public Object getKey(T item) {
                return idProvider.apply(item);
            }

        });

        listBox.addValueChangeHandler(new ValueChangeHandler<T>() {
            @Override
            public void onValueChange(ValueChangeEvent<T> event) {
                SelectionChangeEvent.fire(ValueIdListBox.this);

            }
        });

        initWidget(listBox);
    }

    /*===========================================[ CLASS METHODS ]================*/

    public void setAcceptableValues(List<T> list) {
        acceptableValues = Lists.newArrayList(list);
        acceptableValues.add(0, null);

        listBox.setAcceptableValues(acceptableValues);

        updateListBox();
    }

    private void updateListBox() {
        if (value != null) {
            if (acceptableValues != null) {
                for (T t : acceptableValues) {
                    if (value.equals(idProvider.apply(t))) {
                        listBox.setValue(t, false);
                        return;
                    }
                }
            }
        } else {
            listBox.setValue(null, false);
        }
    }

    /*===========================================[ INTERFACE METHODS ]============*/

   /* @Override
    public void setFormGroup(Widget controlGroup) {
        this.controlGroup = controlGroup;
    }

    @Override
    public void setErrorLabel(Widget errorLabel) {
        this.errorLabel = errorLabel;
    }

    @Override
    public void setErrorLabelText(String errorMessage) {
        ValidationUtils.setErrorText(errorMessage, errorLabel);
    }

    @Override
    public void showErrors(List<EditorError> errors) {
        ValidationUtils.showErrors(errors, controlGroup, this, this);
    }
*/
    @Override
    public boolean isEnabled() {
        return listBox.isVisible();
    }

    @Override
    public void setEnabled(boolean enabled) {
        listBox.setVisible(enabled);
    }

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return addHandler(handler, SelectionChangeEvent.getType());
    }

    @Override
    public ID getValue() {
        return idProvider.apply(listBox.getValue());
    }

    @Override
    public void setValue(ID value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final ID value, final boolean fireEvents) {
        this.value = value;

        updateListBox();

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ID> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setStyleName(String style) {
        listBox.setStyleName(style);
    }

}
