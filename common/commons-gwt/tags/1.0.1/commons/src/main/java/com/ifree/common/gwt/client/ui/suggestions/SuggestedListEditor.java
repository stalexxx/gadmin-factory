package com.ifree.common.gwt.client.ui.suggestions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.client.ui.widgets.BagePanel;
import com.ifree.common.gwt.shared.ValueProvider;

import java.util.List;
import java.util.Set;

/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
public class SuggestedListEditor<T> extends Composite implements LeafValueEditor<List<T>>, IsEditor<LeafValueEditor<List<T>>>,
        HasValue<List<T>>, HasEnabled {

    /*===========================================[ STATIC VARIABLES ]=============*/

    BagePanel<T> bagePanel;

    SuggestedEditor<T> suggestEditor;

    private Renderer<T> renderer;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Deprecated
    public SuggestedListEditor(Renderer<T> renderer) {
        this(renderer, null, null);
    }

    public SuggestedListEditor(Renderer<T> renderer, BaseDataProxy<T> dataProxy,
                               ValueProvider<T, String> searchField) {

        bagePanel = new BagePanel<T>(renderer);
        this.renderer = renderer;

        suggestEditor = new SuggestedEditor<T>(renderer, dataProxy, searchField);

        suggestEditor.addSelectionHandler(new SelectionHandler<T>() {
            @Override
            public void onSelection(SelectionEvent<T> event) {
                suggestEditor.setValue(null);

                final Set<T> value = Sets.newLinkedHashSet(getValue());
                final T item = event.getSelectedItem();

                if (item != null) {
                    value.add(item);
                    setValue(Lists.newArrayList(value), true);
                }
            }
        });

        FlowPanel panel = new FlowPanel();
        panel.add(suggestEditor);
        panel.add(bagePanel);

        initWidget(panel);
    }


    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void setValue(List<T> value, boolean fireEvents) {
        bagePanel.setValue(value, fireEvents);
    }

    @Override
    public List<T> getValue() {
        return bagePanel.getValue();
    }

    @Override
    public void setValue(List<T> value) {
        setValue(value, false);
    }

    @Override
    public LeafValueEditor<List<T>> asEditor() {
        return this;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<T>> handler) {
        return bagePanel.addValueChangeHandler(handler);
    }

    @Override
    public boolean isEnabled() {
        return suggestEditor.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestEditor.setEnabled(enabled);
        bagePanel.setEnabled(enabled);
    }

    /*===========================================[ INNER CLASSES ]================*/



}