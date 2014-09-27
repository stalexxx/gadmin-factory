package com.ifree.common.gwt.client.ui.labels;

import com.google.gwt.text.client.DateTimeFormatRenderer;

import java.util.Date;

/**
 * Created by alex on 18.07.14.
 */
public class DateTimeLabel extends RenderedLabel<Date> {
    public DateTimeLabel() {
        super(new DateTimeFormatRenderer(com.google.gwt.i18n.shared.DateTimeFormat.getFormat(com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT)));
    }
}
