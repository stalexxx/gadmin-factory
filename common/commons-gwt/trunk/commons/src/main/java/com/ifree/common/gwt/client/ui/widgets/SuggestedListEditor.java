package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.TextBox;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
public class SuggestedListEditor<T> extends Composite implements LeafValueEditor<List<T>>, IsEditor<LeafValueEditor<List<T>>>,
        HasValue<List<T>>, HasEnabled {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static SuggestedListEditorUiBinder ourUiBinder = GWT.create(SuggestedListEditorUiBinder.class);
    private final SuggestOracle oracle;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @UiField(provided = true)
    BagePanel<T> bagePanel;

    @UiField(provided = true)
    SuggestBox suggestBox;

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

        if (dataProxy == null) {
            oracle = new MultiWordSuggestOracle();
        } else {
            oracle = new BaseSuggestOracle<T>(dataProxy, renderer, searchField);
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
                final Set<T> value = Sets.newLinkedHashSet(getValue());
                final T item;


                if (event.getSelectedItem() instanceof ValuedSuggestion) {
                    ValuedSuggestion<T> selectedItem = (ValuedSuggestion) event.getSelectedItem();
                    item = selectedItem.getValue();
                } else {
                    throw new IllegalStateException();
                    /*final String replacementString = event.getSelectedItem().getReplacementString();
                    item = replacementMap.get(replacementString);
                */
                }

                if (item != null) {
                    value.add(item);
                    setValue(Lists.newArrayList(value), true);
                }

                suggestBox.setValue(null);
            }
        });

        initWidget(ourUiBinder.createAndBindUi(this));
    }


    /*===========================================[ INTERFACE METHODS ]============*/

    /*public void setAcceptableValues(List<T> acceptableValues) {
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
    }*/

    @Override
    public void setValue(List<T> value) {
        setValue(value, false);
    }

    @Override
    public void setValue(List<T> value, boolean fireEvents) {
        bagePanel.setValue(value, fireEvents);
    }


    @Override
    public List<T> getValue() {
        return bagePanel.getValue();
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
        return suggestBox.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestBox.setEnabled(enabled);
        bagePanel.setEnabled(enabled);
    }

    /*===========================================[ INNER CLASSES ]================*/

    interface SuggestedListEditorUiBinder extends UiBinder<HTMLPanel, SuggestedListEditor> {
    }


}