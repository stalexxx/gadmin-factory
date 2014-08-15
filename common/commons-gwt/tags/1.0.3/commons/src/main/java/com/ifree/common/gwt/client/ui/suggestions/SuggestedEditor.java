package com.ifree.common.gwt.client.ui.suggestions;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import com.ifree.common.gwt.client.ui.grids.BaseDataProxy;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;
import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
public class SuggestedEditor<T> extends Composite implements LeafValueEditor<T>, IsEditor<LeafValueEditor<T>>,
        HasConstrainedValue<T>, HasEnabled, HasPlaceholder, HasSelectionHandlers<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private final SuggestOracle oracle;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private T value;

    SuggestBox suggestBox;

    private Renderer<T> renderer;

    private Map<String, T> replacementMap = Maps.newHashMap();
    private Button removeButton;
    private boolean shortListExpected = true;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public SuggestedEditor(Renderer<T> renderer) {
        this(renderer, null, null);
    }

    public SuggestedEditor(Renderer<T> renderer, BaseDataProxy<T> dataProxy, ValueProvider<T, String> searchField) {
        this.renderer = renderer;

        if (dataProxy == null) {
            oracle = new MultiWordSuggestOracle() ;
        } else {
            oracle = new BaseSuggestOracle<T>(dataProxy, renderer, searchField);

        }

        final TextBox box = new TextBox();
        suggestBox = new SuggestBox(oracle,  box, new SuggestionDisplayImpl());
        box.setStyleName("form-control");

        box.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                scheduleShowSuggestList();

            }
        });

        box.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    if (getValue() != null) {
                        setValue(null);
                        scheduleShowSuggestList();
                    }
                }
            }
        });



        suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                if (event.getSelectedItem() instanceof Suggestion) {
                    Suggestion<T> selectedItem = (Suggestion) event.getSelectedItem();
                    setValue( selectedItem.getValue(), true);
                } else {
                    final String replacementString = event.getSelectedItem().getReplacementString();
                    setValue(replacementMap.get(replacementString), true);
                }

                SelectionEvent.fire(SuggestedEditor.this, value);

            }
        });

        InputGroup inputGroup = createGroup(suggestBox);

        initWidget(inputGroup);
    }

    protected void scheduleShowSuggestList() {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                if (getValue() == null && isShortListExpected()) {
                    suggestBox.showSuggestionList();
                }
            }
        });
    }

    private InputGroup createGroup(SuggestBox widget) {
        InputGroup inputGroup = new InputGroup();
        InputGroupButton inputGroupButton = new InputGroupButton();
        removeButton = new Button();
        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (isEnabled()) {
                    setValue(null, true);
                }
            }
        });

        removeButton.setIcon(IconType.TIMES);
        inputGroupButton.add(removeButton);
        inputGroup.add(widget);
        inputGroup.add(inputGroupButton);
        return inputGroup;
    }


    /*===========================================[ INTERFACE METHODS ]============*/

    public void setAcceptableValues(Collection<T> acceptableValues) {

        if (acceptableValues != null) {
            MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) this.oracle;
            oracle.clear();
            replacementMap.clear();

            for (T acceptableValue : acceptableValues) {
                final String render = renderer.render(acceptableValue);
                oracle.add(render);
                replacementMap.put(render, acceptableValue);
            }


            oracle.setDefaultSuggestionsFromText(Collections2.transform(acceptableValues, new Function<T, String>() {
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

    @Override
    public boolean isEnabled() {
        return suggestBox.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestBox.setEnabled(enabled);
        removeButton.setEnabled(enabled);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        ValueBoxBase<String> valueBox = suggestBox.getValueBox();
        if (valueBox instanceof TextBox) {
            TextBox box = (TextBox) valueBox;
            box.setPlaceholder(placeholder);
        }
    }

    @Override
    public String getPlaceholder() {
        ValueBoxBase<String> valueBox = suggestBox.getValueBox();
        if (valueBox instanceof TextBox) {
            TextBox box = (TextBox) valueBox;
            box.getPlaceholder();
        }
        return null;
    }

    public boolean isShortListExpected() {
        return shortListExpected;
    }

    public void setShortListExpected(boolean shortListExpected) {
        this.shortListExpected = shortListExpected;
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<T> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    /*===========================================[ INNER CLASSES ]================*/



}