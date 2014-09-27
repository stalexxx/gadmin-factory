package com.gafactory.core.client.ui.suggestions;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.gafactory.core.client.ui.AbstractAsyncCallback;
import com.gafactory.core.client.ui.grids.BaseDataProxy;
import com.gafactory.core.client.ui.grids.ItemLoader;
import com.gafactory.core.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by alex on 22.05.14.
 */
public class MappedSuggestedEditor<T, ID extends Serializable> extends Composite implements LeafValueEditor<ID>,
        HasValue<ID>, HasEnabled, HasPlaceholder, HasSelectionHandlers<T> {

    private final SuggestedEditor<T> suggestedEditor;

    private final ValueProvider<T, ID> idFunction;

    private final ItemLoader<T, ID> itemLoader;

    public MappedSuggestedEditor(Renderer<T> renderer, ValueProvider<T, ID> idFunction,
                                 ItemLoader<T, ID> itemLoader, BaseDataProxy<T> dataProxy, ValueProvider<T, String> valueProvider) {
        this.idFunction = idFunction;
        this.itemLoader = itemLoader;
        suggestedEditor = new SuggestedEditor<T>(renderer, dataProxy, valueProvider);
        initWidget(suggestedEditor);
    }


    @Override

    public boolean isEnabled() {
        return suggestedEditor.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestedEditor.setEnabled(enabled);
    }

    @Override
    public void setValue(final ID value, final boolean fireEvents) {
        if (value != null) {

            itemLoader.loadOne(value, new AbstractAsyncCallback<T>() {
                @Override
                public void onSuccess(T result) {
                    setValueX(result, fireEvents);
                }
            });
        } else {
            setValueX(null, fireEvents);
        }

    }

    private void setValueX(T result, boolean fireEvents) {
        suggestedEditor.setValue((result));
        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

/*
    public HandlerRegistration addValueChangeHandlerSource(final ValueChangeHandler<T> handler) {
        return suggestedEditor.addHandler(handler, ValueChangeEvent.getType());
    }
*/

    @Override
    public void setValue(ID value) {

        setValue(value, false);
    }

    @Override
    public ID getValue() {
        T value = getSourceValue();
        return value != null ? idFunction.getValue(value) : null;
    }

    public T getSourceValue() {
        return suggestedEditor.getValue();
    }

    public void setAcceptableValues(Collection<T> values) {
        suggestedEditor.setAcceptableValues(values);
    }

    public void setPlaceholder(String placeholder) {
        suggestedEditor.setPlaceholder(placeholder);
    }

    @Override
    public String getPlaceholder() {
        return suggestedEditor.getPlaceholder();
    }

    public void setShortListExpected(boolean shortListExpected) {
        suggestedEditor.setShortListExpected(shortListExpected);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<T> handler) {
        return suggestedEditor.addSelectionHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<ID> handler) {
        return suggestedEditor.addValueChangeHandler(new ValueChangeHandler<T>() {
            @Override
            public void onValueChange(ValueChangeEvent<T> event) {
                handler.onValueChange(new ValueChangeEvent<ID>(getValue()) {

                });
            }
        });
    }

}
