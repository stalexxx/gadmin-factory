package com.ifree.common.gwt.client.ui.labels;

import com.google.gwt.text.shared.AbstractRenderer;

/**
 * Created by alex on 15.07.14.
 */
public class NotFormattedNumberLabel extends RenderedLabel<Number> {
    public NotFormattedNumberLabel() {
        super(new AbstractRenderer<Number>() {
            @Override
            public String render(Number object) {
                return object != null ? String.valueOf(object) : null;
            }
        });
    }
}
