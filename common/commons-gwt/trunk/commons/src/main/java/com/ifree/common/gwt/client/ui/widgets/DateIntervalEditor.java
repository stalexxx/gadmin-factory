package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.ifree.common.gwt.shared.types.DateInterval;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerLanguage;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerPosition;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.events.ChangeDateEvent;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.events.ChangeDateHandler;

import java.util.Date;

/**
 * Created by alex on 08.07.14.
 */
public class DateIntervalEditor extends Composite implements LeafValueEditor<DateInterval>, HasValue<DateInterval> {

    private final DateTimePicker from;
    private final DateTimePicker to;
    public static final String PATTERN = "dd-mm-yy";
    public static final DateTimeFormat FORMAT = DateTimeFormat.getFormat(PATTERN);

    private static final int DAY = 24 * 60 * 60 * 1000;

    public DateIntervalEditor() {
        FlowPanel panel = new FlowPanel();
        from = new DateTimePicker();


        to = new DateTimePicker();

        from.setPlaceholder("c");
        to.setPlaceholder("по");

        SimplePanel sfrom = new SimplePanel(from);
        sfrom.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        SimplePanel sto = new SimplePanel(to);
        sfrom.addStyleName("pull-left");
        sto.addStyleName("pull-left");
        sto.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);

        panel.addStyleName("clearfix");
        Style style = panel.getElement().getStyle();
        style.setWhiteSpace(Style.WhiteSpace.NOWRAP);

        sfrom.setWidth("49%");
        sto.setWidth("49%");

        //from.setStartView(DateTimePickerView.MONTH);
        normalize(from);
        normalize(to);
        from.setPosition(DateTimePickerPosition.TOP_RIGHT);
        to.setPosition(DateTimePickerPosition.TOP_LEFT);

        panel.add(sfrom);
        panel.add(sto);
        initWidget(panel);

        ChangeDateHandler changeDateHandler = new ChangeDateHandler() {
            @Override
            public void onChangeDate(ChangeDateEvent evt) {
                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {
                        ValueChangeEvent.fire(DateIntervalEditor.this, getValue());
                    }
                });
            }
        };
        from.addChangeDateHandler(changeDateHandler);
        to.addChangeDateHandler(changeDateHandler);
    }

    public static void normalize(DateTimePicker picker) {
        picker.setMaxView(DateTimePickerView.MONTH);
        picker.setMinView(DateTimePickerView.MONTH);
        picker.setFormat(PATTERN);
        picker.setHasKeyboardNavigation(true);
        picker.setShowTodayButton(true);
        picker.setAutoClose(true);
        picker.setLanguage(DateTimePickerLanguage.RU);
    }

    @Override
    public void setValue(DateInterval value) {
        setValue(value, false);
    }

    @Override
    public void setValue(DateInterval value, boolean fireEvents) {

        if (value != null) {
            from.setValue(value.getFrom());
            to.setValue(value.getTo() != null ? new Date(value.getTo().getTime() - DAY) : null);
        } else {
            from.setValue(null);
            to.setValue(null);
        }

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public DateInterval getValue() {
        if (from.getValue() != null || to.getValue() != null) {
            return new DateInterval(from.getValue(),
                    to.getValue() != null ? new Date(to.getValue().getTime() + DAY) : null);
        } else {
            return null;
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<DateInterval> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
