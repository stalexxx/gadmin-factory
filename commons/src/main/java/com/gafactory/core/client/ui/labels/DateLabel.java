package com.gafactory.core.client.ui.labels;

import com.google.gwt.text.client.DateTimeFormatRenderer;

import java.util.Date;

/**
 * Created by alex on 18.07.14.
 */
public class DateLabel extends RenderedLabel<Date> {
    public DateLabel() {
        super(new DateTimeFormatRenderer());
    }
}
