package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.Editor;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.DateTimeBoxBase;

import java.util.Date;

public class DateTimeBox extends DateTimeBoxBase implements Editor<Date> {
    @Override
    public Date getValue() {
        return super.getValue();
    }
}