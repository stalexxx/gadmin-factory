package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by alex on 01.08.14.
 */
public class CleareableWrapper<T> extends Composite implements LeafValueEditor<T>, HasValue<T>, HasEnabled, HasPlaceholder {

    final private HasValue<T> peer;
     private HasEnabled hasEnabled;
     private HasPlaceholder hasPlaceholder;


    public static <T> CleareableWrapper<T> of(HasValue<T> peer) {
        return new CleareableWrapper<>(peer);
    }

    protected CleareableWrapper(HasValue<T> peer) {

        this.peer = peer;

        if (peer instanceof HasEnabled) {
            hasEnabled = (HasEnabled) peer;
        }
        if (peer instanceof HasPlaceholder) {
            hasPlaceholder = (HasPlaceholder) peer;

        }

        if (peer instanceof Widget) {
            initWidget(createGroup((Widget) peer));
        }
    }

    private InputGroup createGroup(Widget widget) {
        InputGroup inputGroup = new InputGroup();
        InputGroupButton inputGroupButton = new InputGroupButton();
        Button removeButton = new Button();
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


    @Override
    public void setValue(T value) {
        peer.setValue(value);
    }

    @Override
    public void setValue(T value, boolean fireEvents) {
        peer.setValue(value, fireEvents);
    }

    @Override
    public T getValue() {
        return peer.getValue();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return peer.addValueChangeHandler(handler);
    }

    @Override
    public boolean isEnabled() {
        return hasEnabled == null || hasEnabled.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {

        if (hasEnabled != null) {
            hasEnabled.setEnabled(enabled);

        }
    }

    @Override
    public void setPlaceholder(String s) {
        if (hasPlaceholder != null) {
            hasPlaceholder.setPlaceholder(s);
        }
    }

    @Override
    public String getPlaceholder() {
        return hasPlaceholder != null ? hasPlaceholder.getPlaceholder() : null;
    }
}
