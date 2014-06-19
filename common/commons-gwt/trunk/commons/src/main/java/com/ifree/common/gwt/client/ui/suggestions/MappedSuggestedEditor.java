package com.ifree.common.gwt.client.ui.suggestions;

import com.google.common.base.Function;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.ifree.common.gwt.client.ui.AbstractAsyncCallback;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.client.ui.grids.ItemLoader;
import com.ifree.common.gwt.client.ui.suggestions.SuggestedEditor;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by alex on 22.05.14.
 */
public class MappedSuggestedEditor<T, ID extends Serializable> extends Composite implements LeafValueEditor<ID>,
        HasValue<ID>, HasEnabled, HasPlaceholder {

    private final SuggestedEditor<T> suggestedEditor;

    private final Function<T, ID> idFunction;

    private final ItemLoader<T, ID> itemLoader;

    public MappedSuggestedEditor(Renderer<T> renderer, Function<T, ID> idFunction,
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
            setValueX(null, false);
        }

    }

    private void setValueX(T result, boolean fireEvents) {
        suggestedEditor.setValue((result));
        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ID> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setValue(ID value) {

        setValue(value, false);
    }

    @Override
    public ID getValue() {
        T value = suggestedEditor.getValue();
        return idFunction.apply(value);
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
}
