package com.gafactory.core.client.ui.labels;

import com.google.gwt.text.client.NumberFormatRenderer;

/**
 * Created by alex on 15.07.14.
 */
public class NumberLabel extends RenderedLabel<Number> {
    public NumberLabel() {
        super(new NumberFormatRenderer());
    }
}
