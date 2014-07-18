package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.constants.Toggle;


/**
 * Created by alex on 25.04.14.
 */
public class BooleanEditor extends Composite implements LeafValueEditor<Boolean>, HasValue<Boolean> {

    private final RadioButton yes;
    private final RadioButton no;
    private final RadioButton doesntMatter;


    public BooleanEditor() {
        this("Не важно", "Да", "Нет");
    }

    //@UiConstructor
    public BooleanEditor(String doesntMatterText, String yesText, String noText) {
        ButtonGroup group = new ButtonGroup();
        group.setDataToggle(Toggle.BUTTONS);
        if (doesntMatterText != null) {
            doesntMatter = new RadioButton(doesntMatterText);
            doesntMatter.setActive(true);

            group.add(doesntMatter);
        } else {
            doesntMatter = null;
        }
        yes = new RadioButton(yesText);
        group.add(yes);
        no = new RadioButton(noText);
        group.add(no);

        /*yes.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (Boolean.TRUE.equals(event.getValue())) {
                    ValueChangeEvent.fire(BooleanEditor.this, true);
                }
            }
        });

        no.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (Boolean.TRUE.equals(event.getValue())) {
                    ValueChangeEvent.fire(BooleanEditor.this, false);
                }
            }
        });
*/

        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {
                        ValueChangeEvent.fire(BooleanEditor.this, getValue());

                    }
                });

            }
        };
        yes.addClickHandler(handler);
        no.addClickHandler(handler);

        if (doesntMatter != null) {
            doesntMatter.addClickHandler(handler);
        }


        initWidget(group);

    }


    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        if (doesntMatter != null) {
            doesntMatter.setValue(value == null);
        }

        yes.setValue(Boolean.TRUE.equals(value));
        no.setValue(Boolean.FALSE.equals(value));

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public Boolean getValue() {
        if (no.isActive()) {
            return false;
        } else if (yes.isActive()) {
            return true;
        } else {

            return null;
        }
    }


    public void setUndefined(String undefined) {
        if (doesntMatter != null) {
            doesntMatter.setText(undefined + "?");
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

}
