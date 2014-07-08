package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.ifree.common.gwt.shared.types.DateInterval;

import java.util.Date;

/**
 * Created by alex on 08.07.14.
 */
public class DateIntervalEditor extends Composite implements LeafValueEditor<DateInterval> {

    private final DateBox from;
    private final DateBox to;

    public DateIntervalEditor() {
        FlowPanel panel = new FlowPanel();
        from = new DateBox();
        to = new DateBox();

        panel.add(from);
        panel.add(to);
        initWidget(panel);
    }

    @Override
    public void setValue(DateInterval value) {
        if (value != null) {
            from.setValue(value.getFrom());
            to.setValue(value.getTo());
        } else {
            from.setValue(new Date());
            to.setValue(new Date(

            ));
        }
    }

    @Override
    public DateInterval getValue() {
        return new DateInterval(from.getValue(), to.getValue());
    }
}
