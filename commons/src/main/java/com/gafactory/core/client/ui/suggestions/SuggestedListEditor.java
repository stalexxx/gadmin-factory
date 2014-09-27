package com.gafactory.core.client.ui.suggestions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Callback;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.gafactory.core.client.ui.grids.BaseDataProxy;
import com.gafactory.core.client.ui.grids.RestDataProxy;
import com.gafactory.core.client.ui.widgets.BagePanel;
import com.gafactory.core.shared.ValueProvider;
import com.gafactory.core.shared.loader.PagingLoadResult;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.Well;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
public class SuggestedListEditor<T, ID extends Serializable> extends Composite implements LeafValueEditor<List<T>>, IsEditor<LeafValueEditor<List<T>>>,
        HasValue<List<T>>, HasEnabled {

    /*===========================================[ STATIC VARIABLES ]=============*/

    BagePanel<T> bagePanel;

    SuggestedEditor<T> suggestEditor;
    private RestDataProxy<T, ID, ?> dataProxy;
    private ValueProvider<T, ID> idValueProvider;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Deprecated
    public SuggestedListEditor(Renderer<T> renderer) {
        this(renderer, null, null, null);
    }

    public SuggestedListEditor(Renderer<T> renderer, final RestDataProxy<T, ID, ?> dataProxy,
                               ValueProvider<T, String> searchField, final ValueProvider<T, ID> idValueProvider) {
        this.dataProxy = dataProxy;
        this.idValueProvider = idValueProvider;

        bagePanel = new BagePanel<T>(renderer);

        suggestEditor = new SuggestedEditor<T>(renderer, dataProxy, searchField);

        suggestEditor.addSelectionHandler(new SelectionHandler<T>() {
            @Override
            public void onSelection(SelectionEvent<T> event) {
                suggestEditor.setValue(null);

                final Set<T> value = Sets.newLinkedHashSet(getValue());
                final T item = event.getSelectedItem();

                if (item != null) {
                    value.add(item);
                    ArrayList<T> list = Lists.newArrayList(value);
                    setValue(list, true);
                }
            }
        });

        bagePanel.addValueChangeHandler(new ValueChangeHandler<List<T>>() {
            @Override
            public void onValueChange(ValueChangeEvent<List<T>> event) {
                updateExcluded(event.getValue());
            }
        });

        FlowPanel panel = new FlowPanel();
        panel.add(suggestEditor);

        createAddRemoveAll(dataProxy);

        Well well = new Well();
        well.add(bagePanel);

        panel.add(well);

        initWidget(panel);
    }

    private IsWidget createAddRemoveAll(final BaseDataProxy<T> dataProxy) {
        org.gwtbootstrap3.client.ui.gwt.FlowPanel panel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        InputGroupButton button = new InputGroupButton();
        button.add(new Button("Все", IconType.PLUS_CIRCLE, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dataProxy.load(new Callback<PagingLoadResult<T>, Throwable>() {
                    @Override
                    public void onFailure(Throwable reason) {

                    }

                    @Override
                    public void onSuccess(PagingLoadResult<T> result) {
                        setValue(Lists.newArrayList(result.getData()));
                    }
                });

            }
        }));
        suggestEditor.getInputGroup().add(button);


        InputGroupButton clear = new InputGroupButton();
        clear.add(new Button("Все", IconType.MINUS_CIRCLE, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setValue(Lists.<T>newArrayList());

            }
        }));
        suggestEditor.getInputGroup().add(clear);

        return panel;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public List<T> getValue() {
        return bagePanel.getValue();
    }

    private List<ID> createIdList( List<T> list) {
        if (list != null) {
            ArrayList<ID> ids = Lists.newArrayList();
            for (T t : list) {
                ids.add(idValueProvider.getValue(t));
            }
            return ids;
        }
        return null;
    }

    @Override
    public void setValue(List<T> value, boolean fireEvents) {
        bagePanel.setValue(value, fireEvents);

        updateExcluded(value);
    }

    protected void updateExcluded(List<T> value) {
        List<ID> idList = createIdList(value);
        dataProxy.setExcluded(idList);
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