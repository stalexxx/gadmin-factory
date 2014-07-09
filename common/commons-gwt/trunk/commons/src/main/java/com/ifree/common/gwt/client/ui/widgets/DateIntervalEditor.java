package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.ifree.common.gwt.shared.types.DateInterval;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerLanguage;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;

/**
 * Created by alex on 08.07.14.
 */
public class DateIntervalEditor extends Composite implements LeafValueEditor<DateInterval> {

    private final DateTimePicker from;
    private final DateTimePicker to;
    public static final String PATTERN = "dd-mm-yy";
    public static final DateTimeFormat FORMAT = DateTimeFormat.getFormat(PATTERN);

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

        panel.add(sfrom);
        panel.add(sto);
        initWidget(panel);

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
        if (value != null) {
            from.setValue(value.getFrom());
            to.setValue(value.getTo());
        } else {
            from.setValue(null);
            to.setValue(null);
        }
    }

    @Override
    public DateInterval getValue() {
        if (from.getValue() != null || to.getValue() != null) {
            return new DateInterval(from.getValue(), to.getValue());
        } else {
            return null;
        }
    }
}
