package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

import javax.annotation.Nullable;
import java.util.Collection;
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

    private List<T> list = Lists.newArrayList();

    @UiField
    FlowPanel itemContainer;

    /*@UiField
    Select select;
    */
    @UiField(provided = true)
    SuggestBox suggestBox;

    private Renderer<T> renderer;

    private Map<String, T> replacementMap = Maps.newHashMap();

    /*===========================================[ CONSTRUCTORS ]=================*/

    public SuggestedListEditor(Renderer<T> renderer) {
        this(renderer, null, null);

    }
    public SuggestedListEditor(Renderer<T> renderer, BaseDataProxy<T> dataProxy, ValueProvider<T, String> searchField) {

        this.renderer = renderer;

        if (dataProxy == null) {
            oracle = new MultiWordSuggestOracle() ;
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
                    final String replacementString = event.getSelectedItem().getReplacementString();
                    item = replacementMap.get(replacementString);
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
    public void setValue(List<T> value) {
         setValue(value, false);
    }

    @Override
    public void setValue(List<T> value, boolean fireEvents) {
        list.clear();
        itemContainer.clear();

        if (value != null) {
            list.addAll(value);
            for (T t : value) {

                FlowPanel panel = new FlowPanel();
                panel.addStyleName("pull-left");
                panel.addStyleName(Styles.BADGE);


                Span badge = new Span();

                Anchor anchor = new Anchor();
                setMargin(anchor, 3);
                anchor.setIcon(IconType.TIMES);

                final String render = renderer.render(t);
                badge.setText(render);

                anchor.addClickHandler(new RemoveClickHandler(render));

                panel.add(badge);
                panel.add(anchor);
                setMargin(panel, 2);

                itemContainer.add(panel);
            }
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    private void setMargin(Widget anchor, int value) {
        anchor.getElement().getStyle().setMargin(value, Style.Unit.PX);
    }

    @Override
    public List<T> getValue() {
        return Lists.newArrayList(list);
    }

    @Override
    public LeafValueEditor<List<T>> asEditor() {
        return this;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<T>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isEnabled() {
        return suggestBox.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestBox.setEnabled(enabled);
    }

    /*===========================================[ INNER CLASSES ]================*/

    interface SuggestedListEditorUiBinder extends UiBinder<HTMLPanel, SuggestedListEditor> {
    }

    private class RemoveClickHandler implements ClickHandler {
        private final String render;

        public RemoveClickHandler(String render) {
            this.render = render;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (isEnabled()) {
                final T deleted = replacementMap.get(render);
                if (deleted != null) {
                    final Collection<T> ts = Sets.newHashSet(getValue());
                    ts.remove(deleted);
                    setValue(Lists.newArrayList(ts), true);
                }
            }

        }
    }
}