package com.gafactory.core.client.ui.labels;

import com.google.gwt.text.shared.AbstractRenderer;

/**
 * Created by alex on 15.07.14.
 */
public class BooleanLabel extends RenderedLabel<Boolean> {
    public BooleanLabel() {
        super(new AbstractRenderer<Boolean>() {
            @Override
            public String render(Boolean object) {
                if (object != null) {
                    return object ? "Да" : "Нет";
                }
                return "?";
            }


        });
    }
}
