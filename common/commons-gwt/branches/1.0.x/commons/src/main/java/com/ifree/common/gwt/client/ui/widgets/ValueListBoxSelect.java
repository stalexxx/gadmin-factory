/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SimpleKeyProvider;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link HasConstrainedValue} based on a
 * {@link com.google.gwt.dom.client.SelectElement}.
 * <p>
 * A {@link Renderer Renderer<T>} is used to get user-presentable strings to
 * display in the select element.
 *
 * @param <T> the value type
 */
public class ValueListBoxSelect<T> extends Composite implements
        Focusable, HasConstrainedValue<T>, HasEnabled,
        IsEditor<TakesValueEditor<T>> {

    private final List<T> values = Lists.newArrayList();
    private final List<Option> options = Lists.newArrayList();
    private final Map<Object, Integer> valueKeyToIndex = new HashMap<Object, Integer>();
    private final Renderer<T> renderer;
    private final ProvidesKey<T> keyProvider;

    private TakesValueEditor<T> editor;
    private T value;

    public ValueListBoxSelect(Renderer<T> renderer) {
        this(renderer, new SimpleKeyProvider<T>());
    }

    public ValueListBoxSelect(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
        this.keyProvider = keyProvider;
        this.renderer = renderer;

        initWidget(new Select());

        getSelect().addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                String val = getSelect().getValue();
                if (val == null) {
                    return; // Not sure why this happens during addValue
                }
                int selectedIndex = valueKeyToIndex.get(val);

                T newValue = values.get(selectedIndex);
                setValue(newValue, true);
            }
        });
    }



    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Returns a {@link TakesValueEditor} backed by the ValueListBox.
     */
    public TakesValueEditor<T> asEditor() {
        if (editor == null) {
            editor = TakesValueEditor.of(this);
        }
        return editor;
    }

    @Override
    public int getTabIndex() {
        return 0;//getSelect().getTabIndex();
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean isEnabled() {
        return getSelect().isEnabled();
    }

    public void setAcceptableValues(Collection<T> newValues) {
        options.clear();
        values.clear();
        valueKeyToIndex.clear();
        Select select = getSelect();
        select.clear();

        for (T nextNewValue : newValues) {
            addValue(nextNewValue);
        }

        updateListBox();
    }

    @Override
    public void setAccessKey(char key) {
        //getSelect().setAccessKey(key);
    }

    @Override
    public void setEnabled(boolean enabled) {
        getSelect().setEnabled(enabled);
    }

    @Override
    public void setFocus(boolean focused) {
        //getSelect().setFocus(focused);
    }

    @Override
    public void setTabIndex (int index) {
        //getSelect().setTabIndex(index);
    }

    /**
     * Set the value and display it in the select element. Add the value to the
     * acceptable set if it is not already there.
     */
    public void setValue(T value) {
        setValue(value, false);
    }

    public void setValue(T value, boolean fireEvents) {
        if (value == this.value || (this.value != null && this.value.equals(value))) {
            return;
        }

        T before = this.value;
        this.value = value;
        updateListBox();

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, before, value);
        }
    }

    private void addValue(T value) {
        String key = (String)keyProvider.getKey(value);
        if (valueKeyToIndex.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate value: " + value);
        }

        valueKeyToIndex.put(key, values.size());
        values.add(value);
        Option option = new Option();
        option.setValue(key);

        option.setText(renderer.render(value));

        options.add(option);
        getSelect().add(option);
        assert values.size() == getSelect().getItemCount();
    }

    private Select getSelect() {
        return (Select) getWidget();
    }

    private void updateListBox() {
        Object key = keyProvider.getKey(value);
        Integer index = valueKeyToIndex.get(key);
        if (index == null) {
            addValue(value);
        }

        index = valueKeyToIndex.get(key);
        Option option = options.get(index);
        getSelect().setValue(option);
        getSelect().refresh();
    }
}
