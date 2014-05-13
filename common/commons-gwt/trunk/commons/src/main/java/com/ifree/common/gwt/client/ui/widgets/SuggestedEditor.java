package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.*;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.TextBox;

import javax.annotation.Nullable;
import java.util.*;

/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
public class SuggestedEditor<T> extends Composite implements LeafValueEditor<T>, IsEditor<LeafValueEditor<T>>,
        HasValue<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private final SuggestOracle oracle;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private T value;

    SuggestBox suggestBox;

    private Renderer<T> renderer;

    private Map<String, T> replacementMap = Maps.newHashMap();


    /*===========================================[ CONSTRUCTORS ]=================*/

    public SuggestedEditor(Renderer<T> renderer) {
        this(renderer, null, null);
    }

    public SuggestedEditor(Renderer<T> renderer, BaseDataProxy<T> dataProxy, ValueProvider<T, String> searchField) {
        this.renderer = renderer;

        if (dataProxy == null) {
            oracle = new MultiWordSuggestOracle() ;
        } else {
            oracle = new BaseSuggestOracle(dataProxy, renderer, searchField);

        }
        final TextBox box = new TextBox();
        suggestBox = new SuggestBox(oracle, box);
        box.setStyleName("form-control");

        box.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                suggestBox.showSuggestionList();
            }
        });


        suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                if (event.getSelectedItem() instanceof ValuedSuggestion) {
                    ValuedSuggestion<T> selectedItem = (ValuedSuggestion) event.getSelectedItem();
                    value = selectedItem.getValue();
                } else {
                    final String replacementString = event.getSelectedItem().getReplacementString();
                    final T item = replacementMap.get(replacementString);

                    value = item;
                }
            }
        });

        initWidget(suggestBox);
    }


    /*===========================================[ INTERFACE METHODS ]============*/

    public void setAcceptableValues(List<T> acceptableValues) {

        if (acceptableValues != null) {
            MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) this.oracle;
            oracle.clear();
            replacementMap.clear();

            for (T acceptableValue : acceptableValues) {
                final String render = renderer.render(acceptableValue);
                oracle.add(render);
                replacementMap.put(render, acceptableValue);
            }


            oracle.setDefaultSuggestionsFromText(Lists.transform(acceptableValues, new Function<T, String>() {
                @Nullable
                @Override
                public String apply(@Nullable T input) {
                    return input != null ? renderer.render(input) : null;
                }
            }));
        }
    }

    @Override
    public void setValue(T value) {
         setValue(value, false);
    }

    @Override
    public void setValue(T value, boolean fireEvents) {

        this.value = value;

        suggestBox.setValue(renderer.render(value));
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }


    @Override
    public T getValue() {
        return value;

    }

    @Override
    public LeafValueEditor<T> asEditor() {
        return this;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /*===========================================[ INNER CLASSES ]================*/



}